package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv;
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import miyucomics.hexical.data.LedgerData;
import miyucomics.hexical.registry.HexicalItems;
import miyucomics.hexical.registry.HexicalPotions;
import net.minecraft.entity.EquipmentSlot;
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

	@WrapMethod(method = "canOvercast")
	private boolean canOvercast(Operation<Boolean> original) {
		if (this.caster.getEquippedStack(EquipmentSlot.HEAD).isOf(HexicalItems.LEI) || this.caster.hasStatusEffect(HexicalPotions.WOOLEYED_EFFECT))
			return false;
		return original.call();
	}

	@Inject(method = "sendMishapMsgToPlayer(Lat/petrak/hexcasting/api/casting/eval/sideeffects/OperatorSideEffect$DoMishap;)V", at = @At("HEAD"))
	private void captureMishap(OperatorSideEffect.DoMishap mishap, CallbackInfo ci) {
		Text message = mishap.getMishap().errorMessageWithName((CastingEnvironment) (Object) this, mishap.getErrorCtx());
		if (message != null)
			LedgerData.getLedger(caster).setMishap(message);
	}
}