package dev.tentativerpg.client.npc;

import dev.tentativerpg.TentativeRpg;
import dev.tentativerpg.npc.DialogueNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.PacketDistributor;

@Mod(value = TentativeRpg.ID, dist = Dist.CLIENT)
public final class ClientHooks {
    public ClientHooks() {
        DialogueNetwork.setClientPageHandler(ClientHooks::handle);
    }

    private static SoundInstance voice;

    static void handle(DialogueNetwork.Page page) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            return;
        }
        var actor = mc.level.getEntity(page.entityId());
        if (actor == null) {
            return;
        }

        stopVoice();
        if (mc.screen instanceof RelicDialogueScreen screen && screen.isFor(page)) {
            screen.update(page);
        } else {
            mc.setScreen(new RelicDialogueScreen(page));
        }

        ResourceLocation sound = page.sound().isEmpty() ? null : ResourceLocation.tryParse(page.sound());
        if (sound != null && mc.getSoundManager().getSoundEvent(sound) != null) {
            voice = new SimpleSoundInstance(sound, SoundSource.VOICE, 1.25f, 1.0f,
                    SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.LINEAR,
                    actor.getX(), actor.getEyeY(), actor.getZ(), false);
            mc.getSoundManager().play(voice);
        }
    }

    static void stopVoice() {
        if (voice != null) {
            Minecraft.getInstance().getSoundManager().stop(voice);
            voice = null;
        }
    }

    static void sendChoice(DialogueNetwork.Page page, int choice) {
        if (page.resident().hasChoice(choice)) {
            PacketDistributor.sendToServer(new DialogueNetwork.Choice(page.nonce(), choice));
        }
    }
}
