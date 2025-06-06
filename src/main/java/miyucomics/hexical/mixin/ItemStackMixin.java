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
	@Unique
	private final DecimalFormat format = new DecimalFormat("###,###.##");

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

	@Inject(method = "getTooltip(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipContext;)Ljava/util/List;", at = @At("RETURN"))
	public void addAutograph(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
		ItemStack stack = ((ItemStack) (Object) this);
		NbtCompound nbt = stack.getNbt();
		if (nbt == null)
			return;

		if (CharmedItemUtilities.isStackCharmed(stack)) {
			long maxMedia = CharmedItemUtilities.getMaxMedia(stack);
			long media = CharmedItemUtilities.getMedia(stack);
			var color = TextColor.fromRgb(MediaHelper.mediaBarColor(media, maxMedia));
			var mediamount = Text.literal(CharmedItemUtilities.DUST_AMOUNT.format(media / (float) MediaConstants.DUST_UNIT));
			var percentFull = Text.literal(CharmedItemUtilities.PERCENTAGE.format(100f * media / maxMedia) + "%");
			var maxCapacity = Text.translatable("hexcasting.tooltip.media", CharmedItemUtilities.DUST_AMOUNT.format(maxMedia / (float) MediaConstants.DUST_UNIT));
			mediamount.styled(style -> style.withColor(ItemMediaHolder.HEX_COLOR));
			maxCapacity.styled(style -> style.withColor(ItemMediaHolder.HEX_COLOR));
			percentFull.styled(style -> style.withColor(color));
			cir.getReturnValue().add(Text.translatable("hexical.charmed").styled(style -> style.withColor(ItemMediaHolder.HEX_COLOR)));
			cir.getReturnValue().add(Text.translatable("hexcasting.tooltip.media_amount.advanced", mediamount, maxCapacity, percentFull));
		}

		if (stack.isOf(HexicalBlocks.MEDIA_JAR_ITEM))
			cir.getReturnValue().add(Text.translatable("hexcasting.tooltip.media", format.format(((float) nbt.getCompound("BlockEntityTag").getLong("media")) / ((float) MediaConstants.DUST_UNIT))).styled(style -> style.withColor(ItemMediaHolder.HEX_COLOR)));

		if (stack.getItem() instanceof ItemPackagedHex && nbt.getBoolean("cracked")) {
			cir.getReturnValue().add(Text.translatable("hexical.cracked.cracked").formatted(Formatting.GOLD));
			if (nbt.contains(ItemPackagedHex.TAG_PROGRAM)) {
				MutableText text = Text.empty();
				NbtList entries = nbt.getList(ItemPackagedHex.TAG_PROGRAM, NbtElement.COMPOUND_TYPE);
				entries.forEach(compound -> text.append(IotaType.getDisplay((NbtCompound) compound)));
				cir.getReturnValue().add(Text.translatable("hexical.cracked.program").append(text));
			}
		}

		if (!nbt.contains("autographs"))
			return;
		
		MutableText header = Text.translatable("hexical.autograph.header");
		header.styled(style -> style.withColor(Formatting.GRAY));
		cir.getReturnValue().add(header);

		nbt.getList("autographs", NbtCompound.COMPOUND_TYPE).forEach(element -> {
			NbtCompound compound = (NbtCompound) element;
			String name = compound.getString("name");
			FrozenPigment pigment = FrozenPigment.fromNBT(compound.getCompound("pigment"));
			MutableText output = Text.literal("");
			int steps = name.length();
			for (int i = 0; i < steps; i++) {
				int color = pigment.getColorProvider().getColor(ClientStorage.ticks * 3, new Vec3d(0, i, 0));
				output.append(Text.literal(String.valueOf(name.charAt(i))).styled(style -> style.withColor(color)));
			}
			cir.getReturnValue().add(output);
		});
	}
}