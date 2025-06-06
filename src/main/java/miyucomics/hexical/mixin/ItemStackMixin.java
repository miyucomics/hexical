package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.misc.MediaConstants;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.api.utils.MediaHelper;
import at.petrak.hexcasting.common.items.magic.ItemMediaHolder;
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import miyucomics.hexical.client.ClientStorage;
import miyucomics.hexical.registry.HexicalBlocks;
import miyucomics.hexical.utils.CharmedItemUtilities;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.text.DecimalFormat;
import java.util.List;

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

		NbtCompound charm = nbt.getCompound("charmed");
		int maxMedia = charm.getInt("max_media");
		int media = charm.getInt("media");
		return MediaHelper.mediaBarColor(media, maxMedia);
	}
}