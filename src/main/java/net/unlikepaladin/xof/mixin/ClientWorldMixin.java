package net.unlikepaladin.xof.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.unlikepaladin.xof.event.ClientLeaveEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(method = "disconnect", at = @At("HEAD"))
    private void handleDisconnection(CallbackInfo ci) {
        ClientLeaveEvent.DISCONNECT.invoker().onPlayDisconnect(this.client.getNetworkHandler(), this.client);
    }
}