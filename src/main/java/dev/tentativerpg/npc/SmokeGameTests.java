package dev.tentativerpg.npc;

import com.mojang.authlib.GameProfile;
import dev.tentativerpg.TentativeRpg;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import io.netty.buffer.Unpooled;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.gametest.GameTestHolder;

@GameTestHolder(TentativeRpg.ID)
@PrefixGameTestTemplate(false)
public final class SmokeGameTests {
    @GameTest(template = "smoke_empty")
    public static void spawnAndUnlock(GameTestHelper test) {
        var level = test.getLevel();
        BlockPos anchor = test.absolutePos(new BlockPos(8, 5, 2));
        for (int i = 0; i < Cast.RESIDENTS.size(); i++) {
            BlockPos pos = anchor.offset(i % 4 * 4 - 6, 0, i / 4 * 4 + 4);
            level.setBlockAndUpdate(pos.below(), Blocks.STONE.defaultBlockState());
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            level.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
        }
        var server = level.getServer();
        var source = server.createCommandSourceStack().withLevel(level).withPosition(Vec3.atBottomCenterOf(anchor));
        server.getCommands().performPrefixedCommand(source, "rpgtestingnpc demo");
        server.getCommands().performPrefixedCommand(source, "rpgtestingnpc demo");

        var file = ServerQuestFile.getInstance().orElseThrow();
        var player = new FakePlayer(level, new GameProfile(UUID.randomUUID(), "QuestSmokeTest"));
        for (Cast.Resident resident : Cast.RESIDENTS) {
            int copies = level.getEntitiesOfClass(Mob.class, new AABB(anchor).inflate(32),
                    npc -> Cast.forEntity(npc) == resident).size();
            test.assertTrue(copies == 1, "Expected one Easy NPC for " + resident.id() + ", found " + copies);

            var team = new TeamData(UUID.randomUUID(), file);
            var collect = file.getQuest(resident.questId(2));
            var report = file.getQuest(resident.questId(4));
            test.assertTrue(collect != null && report != null, "Missing quests for " + resident.id());
            test.assertTrue(!collect.isVisible(team) && !team.canStartTasks(collect, false) && !team.canStartTasks(report, false),
                    "Assignment started before acceptance");
            test.assertTrue(QuestBridge.advance(file, team, player, resident, true) == Cast.Line.LOCKED,
                    "Turn-in before accepting the quest");
            test.assertTrue(QuestBridge.advance(file, team, player, resident, false) == Cast.Line.ACCEPTED,
                    "Acceptance failed");
            test.assertTrue(collect.isVisible(team) && team.canStartTasks(collect, false),
                    "Acceptance did not unlock collection");
            test.assertTrue(QuestBridge.advance(file, team, player, resident, false) == Cast.Line.ACTIVE,
                    "Repeated acceptance should preserve progress");
            var task = file.getTask(resident.questId(3));
            if (!(task instanceof NpcSupplyTask supplies)) {
                throw new IllegalStateException("Missing NPC supply task");
            }
            int count = (int) supplies.getMaxProgress();
            ItemStack sample = supplies.getItemStack();
            player.getInventory().setItem(0, sample.copyWithCount(count - 1));
            test.assertTrue(QuestBridge.advance(file, team, player, resident, true) == Cast.Line.WAITING
                    && player.getInventory().getItem(0).getCount() == count - 1,
                    "An incomplete turn-in must leave the supplies in the inventory");
            player.getInventory().setItem(1, sample.copyWithCount(2));
            supplies.submitTask(team, player);
            test.assertTrue(!team.isCompleted(collect) && !supplies.canInsertItem()
                    && supplies.insert(team, sample, false) == sample,
                    "Quest book and FTB task screen submission must be disabled");
            test.assertTrue(QuestBridge.advance(file, team, player, resident, true) == Cast.Line.FINISHED
                    && team.isCompleted(collect) && team.isCompleted(report),
                    "NPC turn-in did not finish the questline");
            test.assertTrue(player.getInventory().getItem(0).isEmpty()
                    && player.getInventory().getItem(1).getCount() == 1,
                    "NPC turn-in should consume exactly the requested supplies");
            test.assertTrue(QuestBridge.advance(file, team, player, resident, true) == Cast.Line.REPORTED
                    && player.getInventory().getItem(1).getCount() == 1,
                    "Repeated turn-in should preserve inventory and progress");
            player.getInventory().setItem(1, ItemStack.EMPTY);
            // Existing saves may already have partial progress from the old quest book item task.
            if (count > 1) {
                var legacyTeam = new TeamData(UUID.randomUUID(), file);
                QuestBridge.advance(file, legacyTeam, player, resident, false);
                legacyTeam.setProgress(supplies, 1);
                player.getInventory().setItem(0, sample.copyWithCount(count));
                test.assertTrue(QuestBridge.advance(file, legacyTeam, player, resident, true) == Cast.Line.FINISHED
                        && player.getInventory().getItem(0).getCount() == 1,
                        "Existing item progress should count toward the NPC turn-in");
                player.getInventory().setItem(0, ItemStack.EMPTY);
            }
        }
        test.succeed();
    }

    @GameTest(template = "smoke_empty")
    public static void externalDefinitions(GameTestHelper test) {
        var level = test.getLevel();
        var harbor = Cast.byId("harbor_quartermaster");
        var lookout = Cast.byId("cliff_lookout");
        test.assertTrue(harbor != null && lookout != null, "External example definitions did not load");
        test.assertTrue(harbor.view().frame() == RelicTheme.TIDE && harbor.view().hasChoice(0)
                && harbor.view().hasChoice(2), "Quest NPC should expose its configured frame and actions");
        var palette = harbor.view().palette();
        test.assertTrue(palette.accent() == 0xFF87E7DD && palette.background() == 0xFF102A31
                && palette.text() == RelicTheme.TIDE.palette.text(),
                "Custom colors should override only the fields supplied by the author");
        test.assertTrue(harbor.sound(Cast.Line.GREETING).isEmpty()
                && !Cast.byId("sable").sound(Cast.Line.GREETING).isEmpty(),
                "Voice should be optional for the shared dialogue panel");
        test.assertTrue(!lookout.hasQuest() && !lookout.view().hasChoice(0) && lookout.view().hasChoice(1)
                && lookout.view().hasChoice(2) && lookout.text(Cast.Line.EXTRA_LORE).contains("lighthouse"),
                "Lore-only NPC should expose its two configured responses");

        BlockPos pos = test.absolutePos(new BlockPos(6, 5, 6));
        level.setBlockAndUpdate(pos.below(), Blocks.STONE.defaultBlockState());
        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        level.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
        var server = level.getServer();
        var source = server.createCommandSourceStack().withLevel(level).withPosition(Vec3.atBottomCenterOf(pos));
        server.getCommands().performPrefixedCommand(source, "rpgtestingnpc spawn harbor_quartermaster");
        var actors = level.getEntitiesOfClass(Mob.class, new AABB(pos).inflate(2),
                actor -> Cast.forEntity(actor) == harbor);
        test.assertTrue(actors.size() == 1, "The custom NPC did not spawn");
        var unbound = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE
                .getOptional(lookout.modelId()).orElseThrow().create(level);
        unbound.moveTo(pos.getX() + 2.5, pos.getY(), pos.getZ() + 0.5);
        level.addFreshEntity(unbound);
        server.getCommands().performPrefixedCommand(source,
                "rpgtestingnpc bind cliff_lookout " + unbound.getUUID());
        test.assertTrue(Cast.forEntity(unbound) == lookout, "Binding should attach dialogue to an existing Easy NPC");
        var greeting = new DialogueNetwork.Page(harbor.view(), actors.getFirst().getId(), UUID.randomUUID(),
                harbor.greeting(), harbor.sound(Cast.Line.GREETING));
        var buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), level.registryAccess());
        try {
            DialogueNetwork.Page.CODEC.encode(buffer, greeting);
            test.assertTrue(DialogueNetwork.Page.CODEC.decode(buffer).equals(greeting),
                    "The client must receive the complete configured dialogue page");
        } finally {
            buffer.release();
        }

        var file = ServerQuestFile.getInstance().orElseThrow();
        var player = new FakePlayer(level, new GameProfile(UUID.randomUUID(), "CustomQuestSmoke"));
        var team = new TeamData(UUID.randomUUID(), file);
        var supplies = (NpcSupplyTask) file.getTask(harbor.questId(3));
        test.assertTrue(supplies != null && supplies.getMaxProgress() == 3, "Custom quest requirements did not load");
        test.assertTrue(QuestBridge.advance(file, team, player, harbor, false) == Cast.Line.ACCEPTED,
                "The custom NPC could not start its quest");
        player.getInventory().setItem(0, supplies.getItemStack().copyWithCount(4));
        test.assertTrue(QuestBridge.advance(file, team, player, harbor, true) == Cast.Line.FINISHED
                && player.getInventory().getItem(0).getCount() == 1,
                "Custom NPC turn-in must finish the quest and consume three strings");
        test.succeed();
    }

}
