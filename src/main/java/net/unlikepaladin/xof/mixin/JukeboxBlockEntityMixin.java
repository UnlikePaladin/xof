package net.unlikepaladin.xof.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import net.unlikepaladin.xof.FoxMusicInterface;
import net.unlikepaladin.xof.XofModClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin extends BlockEntity{

    @Final
    @Shadow
    private DefaultedList<ItemStack> inventory;

    public JukeboxBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Inject(at = @At("HEAD"), method = "setStack", cancellable = true)
    protected void playXof(int slot, ItemStack stack, CallbackInfo ci) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (stack.isIn(ItemTags.MUSIC_DISCS) && this.world != null && blockEntity instanceof JukeboxBlockEntity && !world.getNonSpectatingEntities(FoxEntity.class, new Box(pos).expand(3.0)).isEmpty()) {
            this.inventory.set(slot, stack);
                if (this.world.getBlockState(this.getPos()) == this.getCachedState()) {
                    this.world.setBlockState(this.getPos(),this.getCachedState().with(JukeboxBlock.HAS_RECORD, true), 2);
                    this.world.emitGameEvent(GameEvent.BLOCK_CHANGE, this.getPos(), GameEvent.Emitter.of(null, this.getCachedState()));
                }
            ci.cancel();
        }
    }
}
