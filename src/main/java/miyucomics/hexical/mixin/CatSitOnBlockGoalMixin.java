package miyucomics.hexical.mixin;

import miyucomics.hexical.inits.HexicalBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CatSitOnBlockGoal.class)
class CatSitOnBlockGoalMixin {
	@Inject(method = "isTargetPos(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z", at = @At("RETURN"), cancellable = true)
	void sits(WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue())
			return;
		if (!world.isAir(pos.up())) {
			cir.setReturnValue(false);
			return;
		}
		BlockState state = world.getBlockState(pos);
		if (state.isOf(HexicalBlocks.SENTINEL_BED_BLOCK) && state.get(FacingBlock.FACING) == Direction.UP)
			cir.setReturnValue(true);
	}
}