package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import miyucomics.hexical.init.HexicalItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CastingEnvironment.class, remap = false)
public abstract class CastingEnvironmentMixin {
	@Shadow public abstract @Nullable LivingEntity getCastingEntity();

	@Inject(method = "isEnlightened", at = @At("HEAD"), cancellable = true)
	private void canDoGreatSpells(CallbackInfoReturnable<Boolean> cir) {
		if (this.getCastingEntity() == null)
			return;
		if (this.getCastingEntity() instanceof PlayerEntity player && player.getInventory().armor.get(3).isOf(HexicalItems.LEI))
			cir.setReturnValue(true);
	}
}