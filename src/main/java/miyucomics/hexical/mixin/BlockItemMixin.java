package miyucomics.hexical.mixin;

import miyucomics.hexical.features.amber_seal.AmberSealBlockEntity;
import miyucomics.hexical.inits.HexicalBlocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onPlaced(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V"))
    private void rotateAmberSeal(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        if ((Object) this != HexicalBlocks.AMBER_SEAL_ITEM)
            return;
        if (context.getWorld().getBlockEntity(context.getBlockPos()) instanceof AmberSealBlockEntity seal)
            seal.onPlaced(context);
    }
}