package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import miyucomics.hexical.feature.driver_dot.DriverDotItem;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PatternIota.class, priority = 100, remap = false)
public abstract class PatternIotaMixin {
	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	void substituteDuringRuntime(CastingVM vm, ServerWorld world, SpellContinuation continuation, CallbackInfoReturnable<CastResult> cir) {
		CastResult replacement = DriverDotItem.applyPatternSubstitution(vm, (PatternIota) (Object) this, continuation);
		if (replacement != null)
			cir.setReturnValue(replacement);
	}
}