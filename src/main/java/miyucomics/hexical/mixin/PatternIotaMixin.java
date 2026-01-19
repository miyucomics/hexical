package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import miyucomics.hexical.features.driver_dots.DriverDotItem;
import miyucomics.hexical.features.scarabs.ScarabBeetleItem;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PatternIota.class, priority = 100, remap = false)
public abstract class PatternIotaMixin {
	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	void expandRuntimeMacros(CastingVM vm, ServerWorld world, SpellContinuation continuation, CallbackInfoReturnable<CastResult> cir) {
		PatternIota pattern = (PatternIota) (Object) this;

		CastResult driverDotResult = DriverDotItem.handleDriverDot(vm, pattern, continuation);
		if (driverDotResult != null) {
			cir.setReturnValue(driverDotResult);
			return;
		}

		CastResult scarabResult = ScarabBeetleItem.handleScarab(vm, pattern, continuation);
		if (scarabResult != null)
			cir.setReturnValue(scarabResult);
	}
}