package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.utils.MediaHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Inject(method = "isItemBarVisible", at = @At("HEAD"), cancellable = true)
	public void addCharmedMediaDisplay(CallbackInfoReturnable<Boolean> cir) {
		NbtCompound nbt = ((ItemStack) (Object) this).getNbt();
		if (nbt != null && nbt.contains("charmed"))
			cir.setReturnValue(true);
	}

	@Inject(method = "getItemBarStep", at = @At("HEAD"), cancellable = true)
	public void addCharmedMediaStep(CallbackInfoReturnable<Integer> cir) {
		ItemStack stack = ((ItemStack) (Object) this);
		NbtCompound nbt = stack.getNbt();
		if (nbt != null && nbt.contains("charmed")) {
			NbtCompound charm = nbt.getCompound("charmed");
			int maxMedia = charm.getInt("max_media");
			int media = charm.getInt("media");
			cir.setReturnValue(MediaHelper.mediaBarWidth(media, maxMedia));
		}
	}

	@Inject(method = "getItemBarColor", at = @At("HEAD"), cancellable = true)
	public void addCharmedMediaColor(CallbackInfoReturnable<Integer> cir) {
		NbtCompound nbt = ((ItemStack) (Object) this).getNbt();
		if (nbt != null && nbt.contains("charmed"))
			cir.setReturnValue(0xff_e83d72);
	}
}