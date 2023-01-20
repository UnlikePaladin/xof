package net.unlikepaladin.xof;

import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface FoxMusicInterface {

    default void setNearbySongPlaying(BlockPos songPosition, boolean playing) {

    }

    default boolean isSongPlaying() {
        return false;
    }

}
