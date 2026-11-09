package dev.tentativerpg.client.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.puffish.skillsmod.client.rendering.ConnectionBatchedRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.puffish.skillsmod.client.rendering.ConnectionBatchedRenderer", remap = false)
abstract class ConnectionRendererMixin {
    @ModifyVariable(method = "emitConnection", at = @At("HEAD"), argsOnly = true)
    private boolean everspring$renderWithoutArrowheads(boolean bidirectional) {
        // This flag is local to the renderer. The skill graph retains its directions.
        return true;
    }

    private static final String EMIT_LINE =
        "Lnet/puffish/skillsmod/client/rendering/ConnectionBatchedRenderer;emitLine("
            + "Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;Lorg/joml/Matrix4f;IFFFFF)V";

    @Redirect(method = "emitConnection", at = @At(value = "INVOKE", target = EMIT_LINE, ordinal = 1))
    private void everspring$skipInnerLine(ConnectionBatchedRenderer renderer, Int2ObjectMap<?> batch,
                                          Matrix4f pose, int color, float x1, float y1,
                                          float x2, float y2, float width) {
        // The first pass already draws the complete connector.
    }

    @ModifyConstant(method = "emitConnection", constant = @Constant(floatValue = 3.0F))
    private float everspring$thinConnectionOutline(float width) {
        return 2.0F;
    }
}
