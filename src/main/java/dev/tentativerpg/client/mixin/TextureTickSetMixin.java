package dev.tentativerpg.client.mixin;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.Tickable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureManager.class)
abstract class TextureTickSetMixin {
    @Shadow @Final @Mutable
    private Set<Tickable> tickableTextures;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void tentativerpg$allowTextureRegistrationDuringTick(CallbackInfo callback) {
        // A mod can add a tickable texture during the first world ticks.
        Set<Tickable> concurrentTextures = ConcurrentHashMap.newKeySet();
        concurrentTextures.addAll(tickableTextures);
        tickableTextures = concurrentTextures;
    }
}
