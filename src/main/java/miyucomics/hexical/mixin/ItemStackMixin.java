package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.pigment.FrozenPigment;
import miyucomics.hexical.client.ClientStorage;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Inject(method = "getTooltip(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipContext;)Ljava/util/List;", at = @At("RETURN"))
	public void addAutograph(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
		NbtCompound nbt = ((ItemStack) (Object) this).getNbt();
		if (nbt == null)
			return;
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
				int color = pigment.getColorProvider().getColor(ClientStorage.time * 3, new Vec3d(0, i, 0));
				output.append(Text.literal(String.valueOf(name.charAt(i))).styled(style -> style.withColor(color)));
			}
			cir.getReturnValue().add(output);
		});
	}
}