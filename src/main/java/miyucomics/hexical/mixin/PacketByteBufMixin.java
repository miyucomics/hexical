package miyucomics.hexical.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import miyucomics.hexical.inits.HexicalItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PacketByteBuf.class)
public class PacketByteBufMixin {
	@WrapOperation(method = "writeItemStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getNbt()Lnet/minecraft/nbt/NbtCompound;"))
	NbtCompound dontSyncGrimoireData(ItemStack instance, Operation<NbtCompound> original) {
		NbtCompound og = original.call(instance);
		if (og != null && instance.isOf(HexicalItems.GRIMOIRE_ITEM))
			og.remove("expansions");
		return og;
	}
}