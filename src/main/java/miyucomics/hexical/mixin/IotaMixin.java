package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import miyucomics.hexical.feature.driver_dot.DriverDotItem;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Iota.class, priority = 100, remap = false)
public abstract class IotaMixin {
	@Shadow public abstract boolean executable();

	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	void substituteDuringRuntime(CastingVM vm, ServerWorld world, SpellContinuation continuation, CallbackInfoReturnable<CastResult> cir) {
		if (!executable()) {
			CastResult replacement = DriverDotItem.applyNonexecutableSubstituion(vm, (Iota) (Object) this, continuation);
			if (replacement != null)
				cir.setReturnValue(replacement);
		}
	}
}