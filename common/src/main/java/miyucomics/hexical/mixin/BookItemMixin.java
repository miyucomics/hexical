package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.item.IotaHolderItem;
import at.petrak.hexcasting.api.spell.iota.DoubleIota;
import at.petrak.hexcasting.api.spell.iota.Iota;
import net.minecraft.item.BookItem;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BookItem.class)
public abstract class BookItemMixin implements IotaHolderItem {
	@Override
	public boolean canWrite(ItemStack stack, @Nullable Iota iota) {
		if (iota instanceof DoubleIota)
			return iota.;
		return false;
	}

	@Override
	public void writeDatum(ItemStack stack, @Nullable Iota iota) {

	}
}