package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.ExecutionClientView;
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.iota.Iota;
import com.llamalad7.mixinextras.sugar.Local;
import miyucomics.hexical.features.player.fields.MediaLogField;
import miyucomics.hexical.features.player.fields.MediaLogFieldKt;
import miyucomics.hexical.misc.HexInjectionHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = CastingVM.class, priority = 100, remap = false)
public class CastingVMMixin {
	@Inject(method = "queueExecuteAndWrapIota", at = @At("HEAD"), cancellable = true)
	void expandGrimoire(Iota iota, ServerWorld world, CallbackInfoReturnable<ExecutionClientView> cir) {
		ExecutionClientView view = HexInjectionHelper.handleGrimoire((CastingVM) (Object) this, iota, world);
		if (view != null)
			cir.setReturnValue(view);
	}

	@Inject(method = "performSideEffects", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/casting/eval/sideeffects/OperatorSideEffect;performEffect(Lat/petrak/hexcasting/api/casting/eval/vm/CastingVM;)V"))
	void captureStack(List<? extends OperatorSideEffect> sideEffects, CallbackInfo ci, @Local OperatorSideEffect sideEffect) {
		if (sideEffect instanceof OperatorSideEffect.DoMishap) {
			CastingVM vm = (CastingVM) (Object) this;
			CastingEnvironment env = vm.getEnv();
			if (!MediaLogField.isEnvCompatible(env))
				return;
			MediaLogFieldKt.getMediaLog((ServerPlayerEntity) env.getCastingEntity()).saveStack(vm.getImage().getStack());
		}
	}
}