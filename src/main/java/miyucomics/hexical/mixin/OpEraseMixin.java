package miyucomics.hexical.mixin;

import at.petrak.hexcasting.common.casting.actions.spells.OpErase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(value = OpErase.class, remap = false)
public class OpEraseMixin {
	@ModifyArg(method = "execute", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;getHeldItemToOperateOn(Ljava/util/function/Predicate;)Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment$HeldItemInfo;"), index = 0)
	private Predicate<ItemStack> autographedItemsAreNotEraseable(Predicate<ItemStack> originalPredicate) {
		Predicate<ItemStack> autographPredicate = it -> it.getNbt() == null || !it.getOrCreateNbt().contains("autographs");
		return (autographPredicate).and(originalPredicate);
	}
}