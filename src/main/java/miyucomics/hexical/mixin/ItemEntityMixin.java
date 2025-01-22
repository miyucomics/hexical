package miyucomics.hexical.mixin;

import miyucomics.hexical.inits.HexicalItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
	@Shadow public abstract ItemStack getStack();

	@Shadow private int pickupDelay;

	@Inject(method = "onPlayerCollision", at = @At("HEAD"))
	void deactivateDroppedLamp(CallbackInfo ci) {
		if (!((Entity) (Object) this).getWorld().isClient && this.getStack().getItem() == HexicalItems.ARCH_LAMP_ITEM && this.getStack().getNbt() != null && this.pickupDelay == 0)
			this.getStack().getNbt().putBoolean("active", false);
	}
}