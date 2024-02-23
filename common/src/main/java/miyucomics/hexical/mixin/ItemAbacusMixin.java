package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.spell.iota.DoubleIota;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.utils.NBTHelper;
import at.petrak.hexcasting.common.items.ItemAbacus;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemAbacus.class)
public class ItemAbacusMixin {
	@Shadow @Final public static String TAG_VALUE;

	@Inject(method = "canWrite", at = @At(value = "HEAD"), cancellable = true)
	public void makeWriteable(ItemStack stack, Iota datum, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(datum instanceof DoubleIota);
	}

	@Inject(method = "writeDatum", at = @At(value = "HEAD"))
	public void makeWriteable(ItemStack stack, Iota datum, CallbackInfo ci) {
		if (datum instanceof DoubleIota doub)
			NBTHelper.putDouble(stack, TAG_VALUE, doub.getDouble());
	}
}