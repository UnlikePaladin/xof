package net.unlikepaladin.xof;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.YoutubeProgressCallback;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MusicDownloader {
    private static final String videoId = "c2OO-TRuvDQ";
    // init downloader with default config

    private static final File xofFile = new File(FabricLoader.getInstance().getConfigDir() + "/xof/xofVideo.mp4");
    public static boolean dowloadFunkyVideo() {
        if (xofFile.exists()) {
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
        Thread newThread = new Thread(() -> {
            YoutubeDownloader downloader = new YoutubeDownloader();
            RequestVideoInfo requestVideoInfo = new RequestVideoInfo(videoId);
            Response<VideoInfo> videoInfoResponse = downloader.getVideoInfo(requestVideoInfo);
            VideoInfo video = videoInfoResponse.data();

            List<VideoFormat> videoFormats = video.videoFormats();


            Format format = videoFormats.get(0);
            RequestVideoFileDownload requestVideoFileDownload = new RequestVideoFileDownload(format)
                    .callback(new YoutubeProgressCallback<File>() {
                        @Override
                        public void onDownloading(int progress) {
                        }

                        @Override
                        public void onFinished(File videoInfo) {
                            XofMod.LOGGER.info("Finished file: " + videoInfo);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            XofMod.LOGGER.error("Error: " + throwable.getLocalizedMessage());
                        }
                    })
                    .async()
                    .saveTo(file) // by default "videos" directory
                    .renameTo("xofVideo") // by default file name will be same as video title on youtube
                    .overwriteIfExists(true);

            Response<File> fileResponse = downloader.downloadVideoFile(requestVideoFileDownload);
            File data = fileResponse.data();
        });
        newThread.start();
        return true;
    }

    public static Path getXof() {
        return xofFile.toPath();
    }
}
