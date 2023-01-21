package net.unlikepaladin.xof.audio;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.Pcm16AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import net.unlikepaladin.xof.audio.AudioOutput;

import javax.sound.sampled.DataLine;

public class MusicPlayer {
    private final AudioPlayerManager audioPlayerManager;
    private final AudioDataFormat audioDataFormat;
    private final AudioPlayer audioPlayer;
    private AudioOutput audioOutput;
    private IOutputConsumer outputConsumer;

    public MusicPlayer() {
        audioPlayerManager = new DefaultAudioPlayerManager();
        audioDataFormat = new Pcm16AudioDataFormat(2, 48000, 960, true);
        audioPlayer = audioPlayerManager.createPlayer();
        audioOutput = new AudioOutput(this);
        setup();
    }

    private void setup() {
        audioPlayerManager.setFrameBufferDuration(1000);
        audioPlayerManager.setPlayerCleanupThreshold(Long.MAX_VALUE);

        audioPlayerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        audioPlayerManager.getConfiguration().setOpusEncodingQuality(10);
        audioPlayerManager.getConfiguration().setOutputFormat(audioDataFormat);

        audioPlayerManager.registerSourceManager(new LocalAudioSourceManager());
    }

    public AudioPlayerManager getAudioPlayerManager() {
        return audioPlayerManager;
    }

    public AudioDataFormat getAudioDataFormat() {
        return audioDataFormat;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public IOutputConsumer getOutputConsumer() {
        return outputConsumer;
    }


    public void startAudioOutput() {
        audioOutput.start();
    }

    public void setMixer(String name) {
        audioOutput.setMixer(name);
    }

    public String getMixer() {
        return audioOutput.getMixer();
    }

    public DataLine.Info getSpeakerInfo() {
        return audioOutput.getSpeakerInfo();
    }

    public void setVolume(int volume) {
        audioPlayer.setVolume(volume);
    }

    public int getVolume() {
        return audioPlayer.getVolume();
    }

    public void setOutputConsumer(IOutputConsumer consumer) {
        outputConsumer = consumer;
    }

}
