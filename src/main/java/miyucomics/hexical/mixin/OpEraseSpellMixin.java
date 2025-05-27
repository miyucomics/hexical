package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.common.casting.actions.spells.OpErase;
import com.llamalad7.mixinextras.sugar.Local;
import miyucomics.hexical.utils.CharmedItemUtilities;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "at.petrak.hexcasting.common.casting.actions.spells.OpErase$Spell", remap = false)
public abstract class OpEraseSpellMixin {
	@Final
	@Shadow
	private ItemStack stack;

	@Inject(method = "cast(Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;)V", at = @At(value = "HEAD"))
	private void removeCharm(CastingEnvironment env, CallbackInfo ci) {
		if (CharmedItemUtilities.isStackCharmed(stack))
			CharmedItemUtilities.removeCharm(stack);
	}
}