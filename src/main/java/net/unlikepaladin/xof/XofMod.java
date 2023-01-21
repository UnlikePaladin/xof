package net.unlikepaladin.xof;

import com.sedmelluq.discord.lavaplayer.container.MediaContainerDescriptor;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioTrack;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.fabricmc.api.ModInitializer;
import net.unlikepaladin.xof.audio.MusicPlayer;
import net.unlikepaladin.xof.audio.TrackScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XofMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("xof");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Xof world!");
	}
}
