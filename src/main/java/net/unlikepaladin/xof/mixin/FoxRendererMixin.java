package net.unlikepaladin.xof.mixin;

import net.minecraft.client.render.entity.FoxEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.passive.FoxEntity;
import net.unlikepaladin.xof.FoxMusicInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoxEntityRenderer.class)
public abstract class FoxRendererMixin {
    @Inject(at = @At("TAIL"), method = "setupTransforms(Lnet/minecraft/entity/passive/FoxEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V")
    protected void setupTransforms(FoxEntity foxEntity, MatrixStack matrixStack, float animationProgress, float bodyYaw, float tickDelta, CallbackInfo ci) {
        if (((FoxMusicInterface)foxEntity).isSongPlaying()) {
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(((foxEntity.world.getTime() + tickDelta) * 8)));
        }
    }
}
