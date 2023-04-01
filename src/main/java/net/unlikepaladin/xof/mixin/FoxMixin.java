package net.unlikepaladin.xof.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.unlikepaladin.xof.FoxMusicInterface;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoxEntity.class)
public abstract class FoxMixin extends Entity implements FoxMusicInterface {

	private boolean songPlaying;
	@Nullable
	private BlockPos songSource;

	public FoxMixin(EntityType<?> type, World world) {
		super(type, world);
	}


	@Override
	public void setNearbySongPlaying(BlockPos songPosition, boolean playing) {
		this.songSource = songPosition;
		this.songPlaying = playing;
	}

	@Override
	public boolean isSongPlaying() {
		return this.songPlaying;
	}

	@Inject(at = @At("HEAD"), method = "tickMovement()V")
	private void addSongMethods(CallbackInfo info) {
		if (this.songSource == null || !this.songSource.isWithinDistance(this.getPos(), 3.46) || !(this.world.getBlockState(this.songSource).getBlock() == Blocks.JUKEBOX)) {
			this.songPlaying = false;
			this.songSource = null;
		}
	}
}
