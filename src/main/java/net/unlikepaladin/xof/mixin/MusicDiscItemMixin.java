package net.unlikepaladin.xof.mixin;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.unlikepaladin.xof.NetworkXof;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Stream;

@Mixin(MusicDiscItem.class)
public class MusicDiscItemMixin {

    @Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
    protected void playXof(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        ItemStack itemStack = context.getStack();
        BlockState state = world.getBlockState(blockPos);
        List<FoxEntity> foxEntities;
        if (blockEntity instanceof JukeboxBlockEntity && !world.getNonSpectatingEntities(FoxEntity.class, new Box(blockPos).expand(3.0)).isEmpty()) {
            if (!world.isClient) {
                ((JukeboxBlock) Blocks.JUKEBOX).setRecord(world, blockPos, state, itemStack);
                itemStack.decrement(1);
                PlayerEntity playerEntity = context.getPlayer();
                if (playerEntity != null) {
                    playerEntity.incrementStat(Stats.PLAY_RECORD);
                }
                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(world,blockPos);
                passedData.writeBlockPos(blockPos);
                // Then we'll send the packet to all the players
                watchingPlayers.forEach(player ->
                        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, NetworkXof.PLAY_XOF, passedData));
            }
            cir.setReturnValue(world.isClient ? ActionResult.SUCCESS : ActionResult.CONSUME);
        }
    }
}
