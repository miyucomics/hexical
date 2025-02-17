package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.ExecutionClientView;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.iota.Iota;
import miyucomics.hexical.utils.InjectionHelper;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CastingVM.class, priority = 100, remap = false)
public class CastingVMMixin {
	@Inject(method = "queueExecuteAndWrapIota", at = @At("HEAD"), cancellable = true)
	void expandGrimoire(Iota iota, ServerWorld world, CallbackInfoReturnable<ExecutionClientView> cir) {
		ExecutionClientView view = InjectionHelper.handleGrimoire((CastingVM) (Object) this, iota, world);
		if (view != null)
			cir.setReturnValue(view);
	}
}