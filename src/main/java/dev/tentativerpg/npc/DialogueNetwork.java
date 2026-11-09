package dev.tentativerpg.npc;

import dev.tentativerpg.TentativeRpg;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class DialogueNetwork {
    // The physical-client entry point installs the receiver before payload registration.
    private static Consumer<Page> pageHandler = page -> {};

    public static void setClientPageHandler(Consumer<Page> handler) {
        pageHandler = handler;
    }

    public record View(String id, String name, String caption, RelicTheme frame, RelicTheme.Palette palette,
                List<String> choices) {
        private static final StreamCodec<RegistryFriendlyByteBuf, RelicTheme.Palette> PALETTE = StreamCodec.composite(
                ByteBufCodecs.INT, RelicTheme.Palette::accent,
                ByteBufCodecs.INT, RelicTheme.Palette::border,
                ByteBufCodecs.INT, RelicTheme.Palette::background,
                ByteBufCodecs.INT, RelicTheme.Palette::text,
                RelicTheme.Palette::new);
        static final StreamCodec<RegistryFriendlyByteBuf, View> CODEC = StreamCodec.composite(
                ByteBufCodecs.stringUtf8(32), View::id,
                ByteBufCodecs.stringUtf8(40), View::name,
                ByteBufCodecs.stringUtf8(40), View::caption,
                ByteBufCodecs.stringUtf8(16).map(RelicTheme::valueOf, RelicTheme::name), View::frame,
                PALETTE, View::palette,
                ByteBufCodecs.stringUtf8(32).apply(ByteBufCodecs.list(3)), View::choices,
                View::new);

        public String captionOr(String fallback) {
            return caption.isEmpty() ? fallback : caption;
        }

        public boolean hasChoice(int slot) {
            return slot >= 0 && slot < choices.size() && !choices.get(slot).isEmpty();
        }
    }

    public record Page(View resident, int entityId, UUID nonce, String text, String sound) implements CustomPacketPayload {
        static final Type<Page> TYPE = new Type<>(TentativeRpg.id("page"));
        static final StreamCodec<RegistryFriendlyByteBuf, Page> CODEC = StreamCodec.composite(
                View.CODEC, Page::resident,
                ByteBufCodecs.VAR_INT, Page::entityId,
                UUIDUtil.STREAM_CODEC, Page::nonce,
                ByteBufCodecs.stringUtf8(200), Page::text,
                ByteBufCodecs.stringUtf8(128), Page::sound,
                Page::new);
        @Override public Type<Page> type() { return TYPE; }
    }

    public record Choice(UUID nonce, int option) implements CustomPacketPayload {
        static final Type<Choice> TYPE = new Type<>(TentativeRpg.id("choice"));
        static final StreamCodec<RegistryFriendlyByteBuf, Choice> CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, Choice::nonce,
                ByteBufCodecs.VAR_INT, Choice::option,
                Choice::new);
        @Override public Type<Choice> type() { return TYPE; }
    }

    static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("5");
        registrar.playToClient(Page.TYPE, Page.CODEC, (page, context) -> pageHandler.accept(page));
        registrar.playToServer(Choice.TYPE, Choice.CODEC,
                (choice, context) -> NpcDialogue.choose((ServerPlayer) context.player(), choice));
    }

    static void send(ServerPlayer player, Page page) {
        PacketDistributor.sendToPlayer(player, page);
    }

    private DialogueNetwork() {}
}
