package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.addldata.ADHexHolder;
import at.petrak.hexcasting.common.casting.actions.spells.OpErase;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.function.Predicate;

@Mixin(value = OpErase.class, remap = false)
public class OpEraseMixin {
	@ModifyArg(method = "execute", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;getHeldItemToOperateOn(Ljava/util/function/Predicate;)Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment$HeldItemInfo;"), index = 0)
	private Predicate<ItemStack> autographedItemsAreNotEraseable(Predicate<ItemStack> originalPredicate) {
		Predicate<ItemStack> autographPredicate = it -> it.getNbt() == null || !it.getOrCreateNbt().contains("autographs");
		return (autographPredicate).and(originalPredicate);
	}
}