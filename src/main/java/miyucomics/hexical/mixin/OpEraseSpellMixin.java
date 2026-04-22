package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "at.petrak.hexcasting.common.casting.actions.spells.OpErase$Spell", remap = false)
public class OpEraseSpellMixin {
	@WrapOperation(method = "cast(Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;)V", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/xplat/IXplatAbstractions;findDataHolder(Lnet/minecraft/item/ItemStack;)Lat/petrak/hexcasting/api/addldata/ADIotaHolder;"))
	private ADIotaHolder eraseSignatures(IXplatAbstractions instance, ItemStack stack, Operation<ADIotaHolder> original) {
		stack.removeSubNbt("autographs");
		return original.call(instance, stack);
	}
}