package dev.tentativerpg;

import dev.tentativerpg.npc.NpcDialogue;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(TentativeRpg.ID)
public final class TentativeRpg {
    public static final String ID = "tentativerpg";

    public TentativeRpg(IEventBus modBus, ModContainer container) {
        NpcDialogue.register(modBus, container);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}
