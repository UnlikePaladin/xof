package net.unlikepaladin.xof.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.FoxEntityModel;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.util.math.MathHelper;
import net.unlikepaladin.xof.FoxMusicInterface;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FoxEntityModel.class)
public class FoxModelMixin<T extends FoxEntity> {
  /*  @Final
    @Shadow
    public ModelPart head;
    @Final
    @Shadow
    private ModelPart body;
    @Final
    @Shadow
    private ModelPart rightHindLeg;
    @Final
    @Shadow
    private ModelPart leftHindLeg;
    @Final
    @Shadow
    private ModelPart rightFrontLeg;
    @Final
    @Shadow
    private ModelPart leftFrontLeg;
    @Final
    @Shadow
    private ModelPart tail;

    @Shadow
    private float legPitchModifier;

    @Inject(method = "animateModel(Lnet/minecraft/entity/passive/FoxEntity;FFF)V", at = @At("TAIL"))
    private void animateModelInject(T foxEntity, float limbAngle, float limbDistance, float tickDelta, CallbackInfo ci) {
       this.youSpinMeRightRound(foxEntity, tickDelta, limbAngle);
    }

    @Inject(method = "animateModel(Lnet/minecraft/entity/passive/FoxEntity;FFF)V", at = @At("HEAD"))
    private void resetYaw(T foxEntity, float limbAngle, float limbDistance, float tickDelta, CallbackInfo ci) {
        this.tail.yaw = 0.0f;
        this.body.yaw = 0.0f;
        this.rightFrontLeg.yaw = 0.0f;
        this.leftFrontLeg.yaw = 0.0f;
        this.rightHindLeg.yaw = 0.0f;
        this.leftHindLeg.yaw = 0.0f;

    }

    private void youSpinMeRightRound(T foxEntity, float tickDelta, float limbAngle) {
        FoxMusicInterface foxMusicInterface = (FoxMusicInterface) foxEntity;
        if (foxMusicInterface.isSongPlaying()) {
            int age = foxEntity.age;
            float l = foxEntity.world.getTime() + tickDelta;
            this.body.yaw = l;
            this.head.yaw = l;

            this.tail.yaw = l;
            this.rightFrontLeg.yaw = l;
            this.leftFrontLeg.yaw = l;
            this.rightHindLeg.yaw = l;
            this.leftHindLeg.yaw = l;
        }

    }

    @Redirect(method = "setAngles*",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/FoxEntityModel;setAngles(Lnet/minecraft/entity/passive/FoxEntity;FFFFF)V"))
    public void setAnglesButMakeItGroovy(FoxEntityModel foxEntityModel, T foxEntity, float f, float g, float h, float i, float j) {
        float k;

        if (!(foxEntity.isSleeping() || foxEntity.isWalking() || foxEntity.isInSneakingPose() || ((FoxMusicInterface)foxEntity).isSongPlaying())) {
            this.head.pitch = j * ((float)Math.PI / 180);
            this.head.yaw = i * ((float)Math.PI / 180);
        }
        if (foxEntity.isSongPlaying()) {
            return;
        }
        if (foxEntity.isSleeping()) {
            this.head.pitch = 0.0f;
            this.head.yaw = -2.0943952f;
            this.head.roll = MathHelper.cos(h * 0.027f) / 22.0f;
        }
        if (foxEntity.isInSneakingPose()) {
            this.body.yaw = k = MathHelper.cos(h) * 0.01f;
            this.rightHindLeg.roll = k;
            this.leftHindLeg.roll = k;
            this.rightFrontLeg.roll = k / 2.0f;
            this.leftFrontLeg.roll = k / 2.0f;
        }
        if (foxEntity.isWalking()) {
            k = 0.1f;
            this.legPitchModifier += 0.67f;
            this.rightHindLeg.pitch = MathHelper.cos(this.legPitchModifier * 0.4662f) * 0.1f;
            this.leftHindLeg.pitch = MathHelper.cos(this.legPitchModifier * 0.4662f + (float)Math.PI) * 0.1f;
            this.rightFrontLeg.pitch = MathHelper.cos(this.legPitchModifier * 0.4662f + (float)Math.PI) * 0.1f;
            this.leftFrontLeg.pitch = MathHelper.cos(this.legPitchModifier * 0.4662f) * 0.1f;
        }

    }*/
}