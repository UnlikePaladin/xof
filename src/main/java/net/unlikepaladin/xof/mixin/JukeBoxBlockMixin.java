package net.unlikepaladin.xof.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import net.unlikepaladin.xof.FoxMusicInterface;
import net.unlikepaladin.xof.XofMod;
import net.unlikepaladin.xof.XofModClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(JukeboxBlock.class)
public class JukeBoxBlockMixin {

    @Inject(at = @At("HEAD"), method = "setRecord", cancellable = true)
    protected void playXof(Entity user, WorldAccess world, BlockPos pos, BlockState state, ItemStack stack, CallbackInfo ci) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof JukeboxBlockEntity jukeboxBlockEntity && !world.getNonSpectatingEntities(FoxEntity.class, new Box(pos).expand(3.0)).isEmpty()) {
            jukeboxBlockEntity.setRecord(stack.copy());
            world.setBlockState(pos, state.with(JukeboxBlock.HAS_RECORD, true), Block.NOTIFY_LISTENERS);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(user, state));
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "removeRecord", cancellable = true)
    protected void stopXof(World world, BlockPos pos, CallbackInfo ci) {
        if (world.isClient && XofModClient.xofFileIsValid && XofModClient.musicPlayer != null) {
            XofModClient.musicPlayer.getAudioPlayer().stopTrack();
        }
    }
}
