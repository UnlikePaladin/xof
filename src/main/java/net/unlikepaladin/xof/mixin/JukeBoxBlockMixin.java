package net.unlikepaladin.xof.mixin;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.unlikepaladin.xof.NetworkXof;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Stream;

@Mixin(JukeboxBlock.class)
public class JukeBoxBlockMixin {
    @Inject(at = @At("HEAD"), method = "onUse", cancellable = true)
    protected void stopXof(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (state.get(JukeboxBlock.HAS_RECORD)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof JukeboxBlockEntity) {
                ((JukeboxBlockEntity)blockEntity).dropRecord();
                stopSong(world, pos);
                cir.setReturnValue(ActionResult.success(world.isClient));
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "onStateReplaced")
    protected void stopXofOnBreak(BlockState state, World world, BlockPos pos, BlockState newState, boolean notify, CallbackInfo ci){
        if (!state.isOf(newState.getBlock()))
            stopSong(world, pos);

    }
    private void stopSong(World world, BlockPos pos) {
        if (!world.isClient) {
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            Stream<ServerPlayerEntity> watchingPlayers = PlayerLookup.tracking((ServerWorld)world, pos).stream();
            passedData.writeBlockPos(pos);
            watchingPlayers.forEach(player ->
                    ServerPlayNetworking.send(player, NetworkXof.STOP_XOF, passedData));
        }
    }
}
