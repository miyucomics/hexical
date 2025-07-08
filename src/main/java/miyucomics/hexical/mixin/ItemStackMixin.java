package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.utils.MediaHelper;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@WrapMethod(method = "isItemBarVisible")
	public boolean addCharmedMediaDisplay(Operation<Boolean> original) {
		ItemStack stack = ((ItemStack) (Object) this);
		NbtCompound nbt = stack.getNbt();
		if (nbt == null)
			return original.call();
		if (!nbt.contains("charmed"))
			return original.call();
		return true;
	}

	@WrapMethod(method = "getItemBarStep")
	public int addCharmedMediaStep(Operation<Integer> original) {
		ItemStack stack = ((ItemStack) (Object) this);
		NbtCompound nbt = stack.getNbt();
		if (nbt == null)
			return original.call();
		if (!nbt.contains("charmed"))
			return original.call();

		NbtCompound charm = nbt.getCompound("charmed");
		int maxMedia = charm.getInt("max_media");
		int media = charm.getInt("media");
		return MediaHelper.mediaBarWidth(media, maxMedia);
	}

	@WrapMethod(method = "getItemBarColor")
	public int addCharmedMediaColor(Operation<Integer> original) {
		ItemStack stack = ((ItemStack) (Object) this);
		NbtCompound nbt = stack.getNbt();
		if (nbt == null)
			return original.call();
		if (!nbt.contains("charmed"))
			return original.call();
		return 0xff_e83d72;
	}
}