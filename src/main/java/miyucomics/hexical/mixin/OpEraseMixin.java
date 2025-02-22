package miyucomics.hexical.mixin;

import at.petrak.hexcasting.common.casting.actions.spells.OpErase;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.function.Predicate;

@Mixin(value = OpErase.class, remap = false)
public class OpEraseMixin {
	@ModifyArg(method = "execute", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;getHeldItemToOperateOn(Ljava/util/function/Predicate;)Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment$HeldItemInfo;"), index = 0)
	private Predicate<ItemStack> autographsAreEraseable(Predicate<ItemStack> originalPredicate) {
		Predicate<ItemStack> autographPredicate = it -> it.getNbt() != null && it.getOrCreateNbt().contains("autographs");
		return autographPredicate.or(originalPredicate);
	}

	@ModifyConstant(method = "execute", constant = @Constant(intValue = 0))
	private int makeAlwaysPass(int constant, @Local ItemStack handStack) {
		return 0;
	}
}