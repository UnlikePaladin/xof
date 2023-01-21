package net.unlikepaladin.xof.audio;


import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormatTools;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.unlikepaladin.xof.XofMod;

import javax.sound.sampled.*;
import java.util.Arrays;

public class AudioOutput extends Thread {

    private final MusicPlayer musicPlayer;

    private final AudioFormat format;
    private final DataLine.Info speakerInfo;

    private Mixer mixer;
    private SourceDataLine souceLine;

    public AudioOutput(MusicPlayer musicPlayer) {
        super("Audio Player");
        this.musicPlayer = musicPlayer;
        format = AudioDataFormatTools.toAudioFormat(musicPlayer.getAudioDataFormat());
        speakerInfo = new DataLine.Info(SourceDataLine.class, format);
        setMixer("");
    }

    @Override
    public void run() {
        try {
            final AudioPlayer player = musicPlayer.getAudioPlayer();
            final AudioDataFormat dataformat = musicPlayer.getAudioDataFormat();

            final AudioInputStream stream = AudioPlayerInputStream.createStream(player, dataformat, dataformat.frameDuration(), false);

            final byte[] buffer = new byte[dataformat.chunkSampleCount * dataformat.channelCount * 2];
            final long frameDuration = dataformat.frameDuration();
            int chunkSize;
            while (true) {
                if (souceLine == null || !souceLine.isOpen()) {
                    closeLine();
                    if (!createLine()) {
                        sleep(500);
                        continue;
                    }
                }
                if (!player.isPaused()) {
                    if ((chunkSize = stream.read(buffer)) >= 0) {
                        souceLine.write(buffer, 0, chunkSize);
                        if (musicPlayer.getOutputConsumer() != null) {
                            musicPlayer.getOutputConsumer().accept(Arrays.copyOf(buffer, buffer.length), chunkSize);
                        }
                    } else {
                        throw new IllegalStateException("Audiostream ended. This should not happen.");
                    }
                } else {
                    souceLine.drain();
                    sleep(frameDuration);
                }
            }
        } catch (final Exception ex) {
            XofMod.LOGGER.error("Crashed when creating thread");
            ex.printStackTrace();
        }
    }

    public void setMixer(String name) {
        if (mixer != null && mixer.getMixerInfo().getName().equals(name)) {
            return;
        }
        final Mixer oldMixer = mixer;
        mixer = findMixer(name, speakerInfo);
        closeLine();
        if (oldMixer != null) {
            if (!hasLinesOpen(oldMixer)) {
                oldMixer.close();
            }
        }
    }

    public String getMixer() {
        if (mixer == null) {
            return null;
        }
        return mixer.getMixerInfo().getName();
    }

    public DataLine.Info getSpeakerInfo() {
        return speakerInfo;
    }

    private boolean createLine() {
        if (mixer != null) {
            try {
                final SourceDataLine line = (SourceDataLine) mixer.getLine(speakerInfo);
                final AudioDataFormat dataFormat = musicPlayer.getAudioDataFormat();
                line.open(format, dataFormat.chunkSampleCount * dataFormat.channelCount * 2 * 5);
                line.start();
                souceLine = line;
                return true;
            } catch (final LineUnavailableException ex) {
            }
        }
        return false;
    }

    private void closeLine() {
        if (souceLine != null) {
            souceLine.flush();
            souceLine.stop();
            souceLine.close();
        }
    }

    private Mixer findMixer(String name, Line.Info lineInfo) {
        Mixer defaultMixer = null;
        for (final Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            final Mixer mixer = AudioSystem.getMixer(mixerInfo);
            if (mixer.isLineSupported(lineInfo)) {
                if (mixerInfo.getName().equals(name)) {
                    return mixer;
                }
                if (defaultMixer == null) {
                    defaultMixer = mixer;
                }
            }
        }
        return defaultMixer;
    }

    public static boolean hasLinesOpen(Mixer mixer) {
        return mixer.getSourceLines().length != 0 || mixer.getTargetLines().length != 0;
    }


}