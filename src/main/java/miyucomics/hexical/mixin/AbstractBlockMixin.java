package miyucomics.hexical.mixin;

import at.petrak.hexcasting.common.lib.HexBlocks;
import at.petrak.hexcasting.common.lib.HexItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
	@Inject(method = "canReplace", at = @At("HEAD"), cancellable = true)
	private void replaceSlate(BlockState blockState, ItemPlacementContext itemPlacementContext, CallbackInfoReturnable<Boolean> cir) {
		if (blockState.isOf(HexBlocks.SLATE))
			cir.setReturnValue(!itemPlacementContext.shouldCancelInteraction() && itemPlacementContext.getStack().isOf(HexItems.SLATE));
	}
}