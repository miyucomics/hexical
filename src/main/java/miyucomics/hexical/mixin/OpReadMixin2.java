package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.common.casting.actions.rw.OpRead;
import miyucomics.hexical.inits.HexicalItems;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = OpRead.class, remap = true)
public class OpReadMixin2 {
	@Inject(method = "execute$lambda$0", at = @At("HEAD"))
	private static void dontEatTwoHexbursts(CastingEnvironment env, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.isOf(HexicalItems.HEXBURST_ITEM))
			stack.increment(1);
	}
}