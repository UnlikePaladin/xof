package net.unlikepaladin.xof.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.*;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.unlikepaladin.xof.XofMod;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class TrackScheduler implements AudioEventListener {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;

    public TrackScheduler(AudioPlayer audioPlayer) {
        this.player = audioPlayer;
        this.queue = new LinkedBlockingDeque<>();
    }

    public void onPlayerPause(AudioPlayer player) {
        // Adapter dummy method
    }

    /**
     * @param player Audio player
     */
    public void onPlayerResume(AudioPlayer player) {
        // Adapter dummy method
    }

    /**
     * @param player Audio player
     * @param track Audio track that started
     */
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        // Adapter dummy method
    }


    /**
     * @param player Audio player
     * @param track Audio track where the exception occurred
     * @param exception The exception that occurred
     */
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        // Adapter dummy method
    }

    /**
     * @param player Audio player
     * @param track Audio track where the exception occurred
     * @param thresholdMs The wait threshold that was exceeded for this event to trigger
     */
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        // Adapter dummy method
    }

    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs, StackTraceElement[] stackTrace) {
        onTrackStuck(player, track, thresholdMs);
    }

    @Override
    public void onEvent(AudioEvent event) {
        if (event instanceof PlayerPauseEvent) {
            onPlayerPause(event.player);
        } else if (event instanceof PlayerResumeEvent) {
            onPlayerResume(event.player);
        } else if (event instanceof TrackStartEvent) {
            onTrackStart(event.player, ((TrackStartEvent) event).track);
        } else if (event instanceof TrackEndEvent) {
            onTrackEnd(event.player, ((TrackEndEvent) event).track, ((TrackEndEvent) event).endReason);
        } else if (event instanceof TrackExceptionEvent) {
            onTrackException(event.player, ((TrackExceptionEvent) event).track, ((TrackExceptionEvent) event).exception);
        } else if (event instanceof TrackStuckEvent) {
            TrackStuckEvent stuck = (TrackStuckEvent) event;
            onTrackStuck(event.player, stuck.track, stuck.thresholdMs, stuck.stackTrace);
        }
    }


    public void queue(AudioTrack track) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        if (!player.startTrack(track, false)) {
            queue.offer(track);
            XofMod.LOGGER.warn("Adding to queue, something probably went really wrong");
        } else {
        }
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        player.startTrack(queue.poll(), false);
    }

    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }
}
