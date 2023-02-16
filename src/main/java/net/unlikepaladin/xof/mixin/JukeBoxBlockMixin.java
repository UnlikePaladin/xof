package net.unlikepaladin.xof.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(JukeboxBlock.class)
public class JukeBoxBlockMixin {
    @Inject(at = @At("HEAD"), method = "onUse", cancellable = true)
    protected void stopXof(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (state.get(JukeboxBlock.HAS_RECORD)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof JukeboxBlockEntity jukeboxBlockEntity) {
                jukeboxBlockEntity.method_49213();
                List<FoxEntity> foxEntities;
                if (world.isClient && XofModClient.xofFileIsValid && XofModClient.musicPlayer != null) {
                    XofModClient.musicPlayer.getAudioPlayer().stopTrack();
                    foxEntities = world.getNonSpectatingEntities(FoxEntity.class, new Box(pos).expand(3.0));
                    if (!foxEntities.isEmpty()) {
                        foxEntities.forEach(fox -> ((FoxMusicInterface)fox).setNearbySongPlaying(pos, false));}
                }
                cir.setReturnValue(ActionResult.success(world.isClient));
            }
        }
    }
}
