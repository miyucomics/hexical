package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv;
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import at.petrak.hexcasting.api.mod.HexConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import miyucomics.hexical.data.LedgerData;
import miyucomics.hexical.registry.HexicalEffects;
import miyucomics.hexical.state.PersistentStateHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerBasedCastEnv.class, remap = false)
public class PlayerBasedCastEnvMixin {
	@Shadow @Final protected ServerPlayerEntity caster;

	@WrapOperation(method = "extractMediaFromInventory", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/mod/HexConfig$CommonConfigAccess;mediaToHealthRate()D"))
	private double richerBlood(HexConfig.CommonConfigAccess instance, Operation<Double> original) {
		if (caster.hasStatusEffect(HexicalEffects.WOOLEYED_EFFECT))
			return original.call(instance) * 2.5;
		return original.call(instance);
	}

	@Inject(method = "sendMishapMsgToPlayer(Lat/petrak/hexcasting/api/casting/eval/sideeffects/OperatorSideEffect$DoMishap;)V", at = @At("HEAD"))
	private void captureMishap(OperatorSideEffect.DoMishap mishap, CallbackInfo ci) {
		Text message = mishap.getMishap().errorMessageWithName((CastingEnvironment) (Object) this, mishap.getErrorCtx());
		if (message != null)
			LedgerData.getLedger(caster).setMishap(message);
	}
}