package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import miyucomics.hexical.utils.HexInjectionHelper;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PatternIota.class, priority = 100, remap = false)
public abstract class PatternIotaMixin {
	@Shadow public abstract HexPattern getPattern();

	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	void scarabsFilch(CastingVM vm, ServerWorld world, SpellContinuation continuation, CallbackInfoReturnable<CastResult> cir) {
		CastResult newResult = HexInjectionHelper.handleScarab(vm, (PatternIota) (Object) this, continuation, world);
		if (newResult != null)
			cir.setReturnValue(newResult);
	}
}