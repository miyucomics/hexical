package miyucomics.hexical.mixin;

import miyucomics.hexical.registry.HexicalItems;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
	@Shadow
	public abstract ItemStack getStack();

	@Inject(method = "tick", at = @At("HEAD"))
	void deactivateDroppedLamp(CallbackInfo ci) {
		if (this.getStack().getItem() == HexicalItems.ARCH_LAMP_ITEM && this.getStack().getNbt() != null)
			this.getStack().getNbt().putBoolean("active", false);
	}
}