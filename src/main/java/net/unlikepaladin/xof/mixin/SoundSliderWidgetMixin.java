package net.unlikepaladin.xof.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.SoundSliderWidget;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.unlikepaladin.xof.XofModClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(SoundSliderWidget.class)
public abstract class SoundSliderWidgetMixin extends SliderWidget {

    @Final
    @Shadow
    private SoundCategory category;

    public SoundSliderWidgetMixin(int x, int y, int width, int height, Text message, double value) {
        super(x, y, width, height, message, value);
    }

    @Inject(method = "applyValue", at = @At("TAIL"))
    private void updateXofVolume(CallbackInfo ci) {
        if (category == SoundCategory.MASTER && XofModClient.musicPlayer != null) {
            XofModClient.musicPlayer.setVolume((int)(this.value * 100));
        }
    }
}
