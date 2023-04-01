package net.unlikepaladin.xof.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.unlikepaladin.xof.XofModClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(SimpleOption.OptionSliderWidgetImpl.class)
public abstract class SoundSliderWidgetMixin<N> extends SliderWidget {
    public SoundSliderWidgetMixin(int x, int y, int width, int height, Text text, double value) {
        super(x, y, width, height, text, value);
    }

    @Final
    @Shadow
    private SimpleOption<N> option;

    @Inject(method = "applyValue", at = @At("TAIL"))
    private void updateXofVolume(CallbackInfo ci) {
        if (Objects.equals(option, MinecraftClient.getInstance().options.getSoundVolumeOption(SoundCategory.MASTER)) && XofModClient.musicPlayer != null)
            XofModClient.musicPlayer.setVolume((int)(this.value * 100));
    }
}
