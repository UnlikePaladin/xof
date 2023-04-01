package net.unlikepaladin.xof.mixin;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.unlikepaladin.xof.NetworkXof;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Stream;

@Mixin(JukeboxBlock.class)
public class JukeBoxBlockMixin {

    @Inject(at = @At("HEAD"), method = "setRecord", cancellable = true)
    protected void playXof(IWorld world, BlockPos pos, BlockState state, ItemStack stack, CallbackInfo ci) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof JukeboxBlockEntity && !world.getNonSpectatingEntities(FoxEntity.class, new Box(pos).expand(3.0)).isEmpty()) {
            ((JukeboxBlockEntity)blockEntity).setRecord(stack.copy());
            world.setBlockState(pos, state.with(JukeboxBlock.HAS_RECORD, true), 2);
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "onBlockRemoved")
    protected void stopXofOnBreak(BlockState state, World world, BlockPos pos, BlockState newState, boolean notify, CallbackInfo ci){
        stopSong(world, pos);
    }

    @Inject(at = @At("HEAD"), method = "removeRecord")
    protected void stopXof(World world, BlockPos pos, CallbackInfo ci) {
        stopSong(world, pos);
    }


    private void stopSong(World world, BlockPos pos) {
        if (!world.isClient) {
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(world, pos);
            passedData.writeBlockPos(pos);
            watchingPlayers.forEach(player ->
                    ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, NetworkXof.STOP_XOF, passedData));
        }
    }
}
