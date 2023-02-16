package net.unlikepaladin.xof.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import net.unlikepaladin.xof.FoxMusicInterface;
import net.unlikepaladin.xof.XofMod;
import net.unlikepaladin.xof.XofModClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(MusicDiscItem.class)
public class MusicDiscItemMixin {

    @Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
    protected void playXof(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        ItemStack itemStack = context.getStack();
        List<FoxEntity> foxEntities;
        if (blockEntity instanceof JukeboxBlockEntity jukeboxBlockEntity && !(foxEntities = world.getNonSpectatingEntities(FoxEntity.class, new Box(blockPos).expand(3.0))).isEmpty()) {
            if (!world.isClient) {
                jukeboxBlockEntity.setStack(itemStack);
                itemStack.decrement(1);
                PlayerEntity playerEntity = context.getPlayer();
                if (playerEntity != null) {
                    playerEntity.incrementStat(Stats.PLAY_RECORD);
                }
            }
            else {
                if (world.isClient() && XofModClient.xofFileIsValid) {
                    XofModClient.loadAndPlayXof();
                }
                foxEntities.forEach(fox -> {
                    ((FoxMusicInterface)fox).setNearbySongPlaying(blockPos, true);
                });
            }
            cir.setReturnValue(ActionResult.success(world.isClient));
        }
    }
}
