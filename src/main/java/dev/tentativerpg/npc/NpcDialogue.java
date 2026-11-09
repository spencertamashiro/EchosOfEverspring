package dev.tentativerpg.npc;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.logging.LogUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.slf4j.Logger;

public final class NpcDialogue {
    // Legacy namespace for authored NPCs, quests, and voice packs.
    public static final String ID = "rpgtestingnpc";
    static final Logger LOGGER = LogUtils.getLogger();
    public static final int SESSION_TICKS = 600;
    public static final double RANGE_SQUARED = 36.0;
    private static final Map<UUID, Session> SESSIONS = new HashMap<>();

    private record Session(ServerLevel level, UUID entity, UUID nonce, long created) {}

    public static void register(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, RelicTheme.SPEC, "rpgtestingnpc-dialogue.toml");
        modBus.addListener(DialogueNetwork::register);
        modBus.addListener((FMLCommonSetupEvent event) -> event.enqueueWork(() -> {
            NpcSupplyTask.register();
            QuestChapters.install();
            var custom = CustomNpcs.load();
            Cast.installCustom(CustomQuestChapter.write(custom) ? custom
                    : custom.stream().filter(npc -> !npc.hasQuest()).toList());
        }));
        NeoForge.EVENT_BUS.addListener(NpcDialogue::interactAt);
        NeoForge.EVENT_BUS.addListener(NpcDialogue::interact);
        NeoForge.EVENT_BUS.addListener(NpcDialogue::commands);
        NeoForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedOutEvent event) ->
                SESSIONS.remove(event.getEntity().getUUID()));
        NeoForge.EVENT_BUS.addListener((ServerStoppedEvent event) -> SESSIONS.clear());
    }

    private NpcDialogue() {}

    static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

    private static void interactAt(PlayerInteractEvent.EntityInteractSpecific event) {
        if (offerDialogue(event, event.getTarget())) {
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    private static void interact(PlayerInteractEvent.EntityInteract event) {
        if (offerDialogue(event, event.getTarget())) {
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    private static boolean offerDialogue(PlayerInteractEvent event, Entity npc) {
        Cast.Resident resident = Cast.forEntity(npc);
        if (resident == null) {
            return false;
        }
        if (event.getHand() == InteractionHand.MAIN_HAND && event.getEntity() instanceof ServerPlayer player
                && player.distanceToSqr(npc) <= RANGE_SQUARED && !player.isSpectator()) {
            Session last = SESSIONS.get(player.getUUID());
            ServerLevel level = player.serverLevel();
            // One right-click can send both an interact-at and a generic interaction packet.
            if (last == null || last.level() != level || !last.entity().equals(npc.getUUID())
                    || level.getGameTime() - last.created() > 4) {
                show(player, npc, resident, Cast.Line.GREETING);
            }
        }
        return true;
    }

    static void choose(ServerPlayer player, DialogueNetwork.Choice choice) {
        Session session = SESSIONS.get(player.getUUID());
        if (session == null || !session.nonce().equals(choice.nonce()) || choice.option() < 0 || choice.option() > 2) {
            return;
        }
        ServerLevel level = player.serverLevel();
        long now = level.getGameTime();
        if (level != session.level() || player.isSpectator() || now - session.created() > SESSION_TICKS) {
            SESSIONS.remove(player.getUUID());
            return;
        }
        Entity npc = level.getEntity(session.entity());
        if (npc == null || !npc.isAlive() || player.distanceToSqr(npc) > RANGE_SQUARED || !player.hasLineOfSight(npc)) {
            SESSIONS.remove(player.getUUID());
            return;
        }
        Cast.Resident resident = Cast.forEntity(npc);
        if (resident == null || !resident.view().hasChoice(choice.option())) {
            return;
        }

        Cast.Line response = switch (choice.option()) {
            case 0 -> QuestBridge.advance(player, resident, false);
            case 1 -> Cast.Line.LORE;
            case 2 -> resident.hasQuest() ? QuestBridge.advance(player, resident, true) : Cast.Line.EXTRA_LORE;
            default -> throw new IllegalStateException();
        };
        show(player, npc, resident, response);
    }

    private static void show(ServerPlayer player, Entity npc, Cast.Resident resident, Cast.Line line) {
        ServerLevel level = player.serverLevel();
        UUID nonce = UUID.randomUUID();
        SESSIONS.put(player.getUUID(), new Session(level, npc.getUUID(), nonce, level.getGameTime()));
        DialogueNetwork.send(player, new DialogueNetwork.Page(resident.view(), npc.getId(), nonce, resident.text(line), resident.sound(line)));
    }

    private static void commands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal(ID)
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("spawn").then(Commands.argument("resident", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                Cast.ids(), builder))
                        .executes(context -> {
                            var source = context.getSource();
                            var resident = Cast.byId(StringArgumentType.getString(context, "resident"));
                            if (resident == null) {
                                source.sendFailure(Component.literal("Unknown resident."));
                                return 0;
                            }
                            if (!spawn(source.getLevel(), BlockPos.containing(source.getPosition()), resident)) {
                                source.sendFailure(Component.literal("Needs two empty blocks over solid ground."));
                                return 0;
                            }
                            source.sendSuccess(() -> Component.literal("Placed " + resident.name()), false);
                            return 1;
                        })))
                .then(Commands.literal("bind").then(Commands.argument("resident", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(Cast.ids(), builder))
                        .then(Commands.argument("npc", EntityArgument.entity()).executes(context -> {
                            var source = context.getSource();
                            var resident = Cast.byId(StringArgumentType.getString(context, "resident"));
                            var npc = EntityArgument.getEntity(context, "npc");
                            if (resident == null) {
                                source.sendFailure(Component.literal("Unknown resident."));
                                return 0;
                            }
                            if (!(npc instanceof Mob) || !BuiltInRegistries.ENTITY_TYPE.getKey(npc.getType())
                                    .getNamespace().equals("easy_npc")) {
                                source.sendFailure(Component.literal("Choose one Easy NPC mob."));
                                return 0;
                            }
                            if (npc.getTags().stream().anyMatch(tag -> tag.startsWith(ID + ".")
                                    && !tag.equals(resident.tag()))) {
                                source.sendFailure(Component.literal("Remove the NPC's previous dialogue tag first."));
                                return 0;
                            }
                            if (!npc.getTags().contains(resident.tag()) && !npc.addTag(resident.tag())) {
                                source.sendFailure(Component.literal("This NPC could not store the dialogue binding."));
                                return 0;
                            }
                            source.sendSuccess(() -> Component.literal("Bound dialogue for " + resident.name()), false);
                            return 1;
                        }))))
                .then(Commands.literal("demo").executes(context -> {
                    var source = context.getSource();
                    var level = source.getLevel();
                    BlockPos anchor = BlockPos.containing(source.getPosition());
                    var nearby = level.getEntitiesOfClass(Mob.class, new AABB(anchor).inflate(32),
                            npc -> Cast.forEntity(npc) != null);
                    int placed = 0;
                    int existing = 0;
                    for (int index = 0; index < Cast.RESIDENTS.size(); index++) {
                        Cast.Resident resident = Cast.RESIDENTS.get(index);
                        if (nearby.stream().anyMatch(npc -> npc.getTags().contains(resident.tag()))) {
                            existing++;
                            continue;
                        }
                        BlockPos pos = anchor.offset(index % 4 * 4 - 6, 0, index / 4 * 4 + 4);
                        if (spawn(level, pos, resident)) {
                            placed++;
                        }
                    }
                    int count = placed;
                    int skipped = existing;
                    source.sendSuccess(() -> Component.literal("Placed " + count + " residents. " + skipped
                            + " already nearby. " + (Cast.RESIDENTS.size() - count - skipped)
                            + " need clear, level ground."), false);
                    return placed + existing;
                })));
    }

    private static boolean spawn(ServerLevel level, BlockPos pos, Cast.Resident resident) {
        if (!level.isEmptyBlock(pos) || !level.isEmptyBlock(pos.above())
                || !level.getBlockState(pos.below()).isSolidRender(level, pos.below())) {
            return false;
        }
        var type = BuiltInRegistries.ENTITY_TYPE.getOptional(resident.modelId()).orElse(null);
        if (type == null || !(type.create(level) instanceof Mob npc)) {
            LOGGER.error("Easy NPC model {} could not be created for {}", resident.model(), resident.id());
            return false;
        }
        npc.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 180, 0);
        npc.setCustomName(Component.literal(resident.name()));
        npc.setCustomNameVisible(true);
        npc.setPersistenceRequired();
        npc.setInvulnerable(true);
        npc.setNoAi(true);
        npc.addTag(resident.tag());
        return level.addFreshEntity(npc);
    }
}
