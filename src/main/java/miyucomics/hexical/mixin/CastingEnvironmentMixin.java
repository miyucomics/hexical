package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import miyucomics.hexical.inits.HexicalBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CastingEnvironment.class)
public abstract class CastingEnvironmentMixin {
	@Inject(method = "isVecInRange", at = @At("HEAD"), cancellable = true)
	void sentinelBed(Vec3d vec, CallbackInfoReturnable<Boolean> cir) {
		if (((CastingEnvironment) (Object) this).getWorld().getBlockState(BlockPos.ofFloored(vec)).isOf(HexicalBlocks.SENTINEL_BED_BLOCK))
			cir.setReturnValue(true);
	}
}