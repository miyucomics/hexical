package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv;
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import miyucomics.hexical.feature.media_log.MediaLogField;
import miyucomics.hexical.feature.media_log.MediaLogFieldKt;
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

	@Inject(method = "sendMishapMsgToPlayer(Lat/petrak/hexcasting/api/casting/eval/sideeffects/OperatorSideEffect$DoMishap;)V", at = @At("HEAD"))
	private void captureMishap(OperatorSideEffect.DoMishap mishap, CallbackInfo ci) {
		CastingEnvironment env = (CastingEnvironment) (Object) this;
		Text message = mishap.getMishap().errorMessageWithName(env, mishap.getErrorCtx());
		if (message != null && MediaLogField.isEnvCompatible(env))
			MediaLogFieldKt.getMediaLog(caster).saveMishap(message);
	}
}