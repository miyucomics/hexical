package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv;
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import miyucomics.hexical.features.media_log.MediaLogField;
import miyucomics.hexical.features.media_log.MediaLogFieldKt;
import miyucomics.hexical.features.periwinkle.WooleyedEffect;
import miyucomics.hexical.inits.HexicalItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerBasedCastEnv.class, remap = false)
public class PlayerBasedCastEnvMixin {
	@Shadow @Final protected ServerPlayerEntity caster;

	@Inject(method = "canOvercast", at = @At("HEAD"), cancellable = true)
	private void canOvercast(CallbackInfoReturnable<Boolean> cir) {
		if (this.caster.getEquippedStack(EquipmentSlot.HEAD).isOf(HexicalItems.LEI) || this.caster.hasStatusEffect(WooleyedEffect.INSTANCE))
			cir.setReturnValue(false);
	}

	@Inject(method = "sendMishapMsgToPlayer(Lat/petrak/hexcasting/api/casting/eval/sideeffects/OperatorSideEffect$DoMishap;)V", at = @At("HEAD"))
	private void captureMishap(OperatorSideEffect.DoMishap mishap, CallbackInfo ci) {
		Text message = mishap.getMishap().errorMessageWithName((CastingEnvironment) (Object) this, mishap.getErrorCtx());
		if (message != null && MediaLogField.isEnvCompatible((CastingEnvironment) (Object) this))
			MediaLogFieldKt.getMediaLog(caster).saveMishap(message);
	}
}