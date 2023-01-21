package net.unlikepaladin.xof.audio;

@FunctionalInterface
public interface IOutputConsumer {

    void accept(byte[] buffer, int chunkSize);

}