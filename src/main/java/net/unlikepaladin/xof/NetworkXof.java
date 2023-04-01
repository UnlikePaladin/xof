package net.unlikepaladin.xof;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.List;

public class NetworkXof {
    public static final Identifier STOP_XOF = new Identifier(XofMod.MOD_ID, "stop_xof");
    public static final Identifier PLAY_XOF = new Identifier(XofMod.MOD_ID, "play_xof");

    public static void registerClientPackets() {
        ClientSidePacketRegistry.INSTANCE.register(STOP_XOF,
                (packetContext, attachedData) -> {
                    BlockPos pos = attachedData.readBlockPos();
                    packetContext.getTaskQueue().execute(() -> {
                        List<FoxEntity> foxEntities;
                        if (XofModClient.xofFileIsValid && XofModClient.musicPlayer != null) {
                            XofModClient.musicPlayer.getAudioPlayer().stopTrack();
                            foxEntities = MinecraftClient.getInstance().world.getNonSpectatingEntities(FoxEntity.class, new Box(pos).expand(3.0));
                            if (!foxEntities.isEmpty()) {
                                foxEntities.forEach(fox -> ((FoxMusicInterface)fox).setNearbySongPlaying(pos, false));}
                        }
                    });
        });

        ClientSidePacketRegistry.INSTANCE.register(PLAY_XOF,
                (packetContext, attachedData) -> {
                    BlockPos blockPos = attachedData.readBlockPos();
                    packetContext.getTaskQueue().execute(() -> {
                        List<FoxEntity> foxEntities;
                        if (XofModClient.xofFileIsValid) {
                            if (XofModClient.musicPlayer != null)
                                XofModClient.musicPlayer.getAudioPlayer().stopTrack();

                            foxEntities = MinecraftClient.getInstance().world.getNonSpectatingEntities(FoxEntity.class, new Box(blockPos).expand(3.0));
                            if (!foxEntities.isEmpty()) {
                                foxEntities.forEach(fox -> {
                                    ((FoxMusicInterface)fox).setNearbySongPlaying(blockPos, true);
                                });
                                XofModClient.loadAndPlayXof();
                            }
                        }
                    });
                });
    }
}
