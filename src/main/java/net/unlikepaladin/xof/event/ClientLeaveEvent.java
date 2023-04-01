package net.unlikepaladin.xof.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface ClientLeaveEvent {
    /**
     * An event for the disconnection of the client play network handler.
     *
     * <p>No packets should be sent when this event is invoked. Backported from Fabric-API-V1
     */
    Event<ClientLeaveEvent> DISCONNECT = EventFactory.createArrayBacked(ClientLeaveEvent.class, callbacks -> (handler, client) -> {
        for (ClientLeaveEvent callback : callbacks) {
            callback.onPlayDisconnect(handler, client);
        }
    });
    void onPlayDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client);
}