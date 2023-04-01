package net.unlikepaladin.xof;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.unlikepaladin.xof.audio.MusicPlayer;
import net.unlikepaladin.xof.audio.TrackScheduler;
import net.unlikepaladin.xof.event.ClientLeaveEvent;

public class XofModClient implements ClientModInitializer {
    public static boolean xofFileIsValid;
    public static TrackScheduler scheduler;
    public static MusicPlayer musicPlayer;

    @Override
    public void onInitializeClient() {
        xofFileIsValid = MusicDownloader.dowloadFunkyVideo();
        NetworkXof.registerClientPackets();
        ClientLeaveEvent.DISCONNECT.register((handler, client) -> {
            if (XofModClient.xofFileIsValid && XofModClient.musicPlayer != null)
                XofModClient.musicPlayer.getAudioPlayer().stopTrack();
        });
    }


    public static void loadAndPlayXof() {
        musicPlayer = new MusicPlayer();

        musicPlayer.setVolume((int)(MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MASTER) * 100));

        scheduler = new TrackScheduler(musicPlayer.getAudioPlayer());
        musicPlayer.getAudioPlayer().addListener(scheduler);;
        musicPlayer.getAudioPlayerManager().loadItem(MusicDownloader.getXof().toString(), new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                scheduler.queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks()) {
                    scheduler.queue(track);
                }
            }

            @Override
            public void noMatches() {
                XofMod.LOGGER.warn("Couldn't find Xof video!");
                // Notify the user that we've got nothing
            }

            @Override
            public void loadFailed(FriendlyException throwable) {
                // Notify the user that everything exploded
                XofMod.LOGGER.error("Something went really wrong!");
                throwable.printStackTrace();
            }
        });
        XofModClient.musicPlayer.startAudioOutput();
    }
}
