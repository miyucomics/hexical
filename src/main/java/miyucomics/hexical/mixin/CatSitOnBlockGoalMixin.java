package miyucomics.hexical.mixin;

import miyucomics.hexical.inits.HexicalBlocks;
import net.minecraft.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CatSitOnBlockGoal.class)
class CatSitOnBlockGoalMixin {
	@Inject(method = "isTargetPos(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z", at = @At("RETURN"), cancellable = true)
	void sits(WorldView worldView, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue())
			return;
		if (!worldView.isAir(blockPos.up())) {
			cir.setReturnValue(false);
			return;
		}
		if (worldView.getBlockState(blockPos).isOf(HexicalBlocks.SENTINEL_BED_BLOCK))
			cir.setReturnValue(true);
	}
}