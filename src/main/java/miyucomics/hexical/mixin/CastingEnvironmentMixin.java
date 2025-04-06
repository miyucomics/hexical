package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.ExecutionClientView;
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv;
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.iota.Iota;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import miyucomics.hexical.data.LedgerData;
import miyucomics.hexical.registry.HexicalEffects;
import miyucomics.hexical.utils.InjectionHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = CastingEnvironment.class, remap = false)
public abstract class CastingEnvironmentMixin {
	@Shadow public abstract @Nullable LivingEntity getCastingEntity();

	@WrapMethod(method = "isEnlightened")
	private boolean canOvercast(Operation<Boolean> original) {
		if (this.getCastingEntity() == null)
			return original.call();
		StatusEffectInstance wooleye = this.getCastingEntity().getStatusEffect(HexicalEffects.WOOLEYED_EFFECT);
		if (wooleye == null)
			return original.call();
		System.out.println(wooleye.getAmplifier());
		return wooleye.getAmplifier() < 1;
	}
}