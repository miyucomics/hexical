package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.EntityIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.common.casting.actions.selectors.OpGetCaster;
import miyucomics.hexical.casting.environments.TurretLampCastEnv;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = OpGetCaster.class, remap = false)
public class OpGetCasterMixin {
	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	private void geniesDeserveCredit(List<? extends Iota> args, CastingEnvironment env, CallbackInfoReturnable<List<Iota>> cir) {
		if (env instanceof TurretLampCastEnv)
			cir.setReturnValue(List.of(new EntityIota(((TurretLampCastEnv) env).getLamp())));
	}
}