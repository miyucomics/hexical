package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import miyucomics.hexical.registry.HexicalPotions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = CastingEnvironment.class, remap = false)
public abstract class CastingEnvironmentMixin {
	@Shadow public abstract @Nullable LivingEntity getCastingEntity();

	@WrapMethod(method = "isEnlightened")
	private boolean canOvercast(Operation<Boolean> original) {
		if (this.getCastingEntity() == null)
			return original.call();
		StatusEffectInstance wooleye = this.getCastingEntity().getStatusEffect(HexicalPotions.WOOLEYED_EFFECT);
		if (wooleye == null)
			return original.call();
		System.out.println(wooleye.getAmplifier());
		return wooleye.getAmplifier() < 1;
	}
}