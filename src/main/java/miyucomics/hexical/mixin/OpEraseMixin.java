package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.addldata.ADHexHolder;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.common.casting.actions.spells.OpErase;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import miyucomics.hexical.utils.CharmedItemUtilities;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;
import java.util.function.Predicate;

@Mixin(value = OpErase.class, remap = false)
public class OpEraseMixin {
	@ModifyArg(method = "execute", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;getHeldItemToOperateOn(Ljava/util/function/Predicate;)Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment$HeldItemInfo;"), index = 0)
	private Predicate<ItemStack> addEraseConditions(Predicate<ItemStack> originalPredicate) {
		return stack -> {
			if (stack.getNbt() != null && stack.getNbt().contains("autographs"))
				return false;
			return CharmedItemUtilities.isStackCharmed(stack) || originalPredicate.test(stack);
		};
	}

	@WrapOperation(method = "execute", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/xplat/IXplatAbstractions;findHexHolder(Lnet/minecraft/item/ItemStack;)Lat/petrak/hexcasting/api/addldata/ADHexHolder;"))
	private ADHexHolder makeCharmedItemsEraseable(IXplatAbstractions instance, ItemStack stack, Operation<ADHexHolder> original) {
		if (CharmedItemUtilities.isStackCharmed(stack))
			return new ADHexHolder() {
				@Override
				public boolean canDrawMediaFromInventory() {
					return false;
				}

				@Override
				public boolean hasHex() {
					return true;
				}

				@Override
				public @Nullable List<Iota> getHex(ServerWorld level) {
					return List.of();
				}

				@Override
				public void writeHex(List<Iota> patterns, @Nullable FrozenPigment pigment, long media) {}

				@Override
				public void clearHex() {}

				@Override
				public @Nullable FrozenPigment getPigment() {
					return null;
				}
			};
		return original.call(instance, stack);
	}
}