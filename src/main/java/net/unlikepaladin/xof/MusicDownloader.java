package net.unlikepaladin.xof;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.Format;
import com.github.kiulian.downloader.model.videos.formats.VideoFormat;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.List;

public class MusicDownloader {
    private static final String videoId = "c2OO-TRuvDQ";
    // init downloader with default config

    private static final Path xofFile = Path.of(FabricLoader.getInstance().getConfigDir() + "/xof/xofVideo.mp4");
    public static boolean dowloadFunkyVideo() {
        if (xofFile.toFile().exists()) {
            return true;
        }
        try {
            URL url = new URL("https://www.google.com/");
            URLConnection connection = url.openConnection();
            connection.connect();
        }
        catch (Exception e) {
            XofMod.LOGGER.warn("No Internet Connection, can't download xof audio!");
            return false;
        }
        Path saveFolder = FabricLoader.getInstance().getConfigDir();
        File file = new File(saveFolder.toFile(), "xof");

        YoutubeDownloader downloader = new YoutubeDownloader();
        RequestVideoInfo requestVideoInfo = new RequestVideoInfo(videoId);
        Response<VideoInfo> videoInfoResponse = downloader.getVideoInfo(requestVideoInfo);
        VideoInfo video = videoInfoResponse.data();

        List<VideoFormat> videoFormats = video.videoFormats();


        Format format = videoFormats.get(0);

// sync downloading
        RequestVideoFileDownload requestVideoFileDownload = new RequestVideoFileDownload(format)
                // optional params
                .saveTo(file) // by default "videos" directory
                .renameTo("xofVideo") // by default file name will be same as video title on youtube
                .overwriteIfExists(true); // if false and file with such name already exits sufix will be added video(1).mp4
        Response<File> fileResponse = downloader.downloadVideoFile(requestVideoFileDownload);
        File data = fileResponse.data();
        return true;
    }

    public static Path getXof() {
        return xofFile;
    }
}
