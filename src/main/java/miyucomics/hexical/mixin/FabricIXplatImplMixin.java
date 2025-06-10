package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.addldata.ADHexHolder;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.fabric.xplat.FabricXplatImpl;
import miyucomics.hexical.utils.CharmedItemUtilities;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = FabricXplatImpl.class, remap = false)
public class FabricIXplatImplMixin {
	@Inject(method = "findHexHolder", at = @At("HEAD"), cancellable = true)
	public void makeCharmedItemsProvideHexHolder(ItemStack stack, CallbackInfoReturnable<ADHexHolder> cir) {
		if (CharmedItemUtilities.isStackCharmed(stack)) {
			cir.setReturnValue(new ADHexHolder() {
				@Override
				public boolean canDrawMediaFromInventory() {
					return false;
				}

				@Override
				public boolean hasHex() {
					return true;
				}

				@Override
				public List<Iota> getHex(ServerWorld level) {
					return CharmedItemUtilities.getHex(stack, level);
				}

				@Override
				public void writeHex(List<Iota> patterns, @Nullable FrozenPigment pigment, long media) { }

				@Override
				public void clearHex() {
					CharmedItemUtilities.removeCharm(stack);
				}

				@Override
				public @Nullable FrozenPigment getPigment() {
					return null;
				}
			});
		}
	}
}