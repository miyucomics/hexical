package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.misc.FrozenColorizer;
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
	public void addPigmentDisplayTooltip (PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
		ItemStack stack = (ItemStack)(Object) this;
		if (stack.getOrCreateNbt().contains("autographs")) {
			MutableText header = Text.translatable("hexical.autograph.header");
			header.styled(style -> style.withColor(Formatting.GRAY));
			cir.getReturnValue().add(header);

			stack.getOrCreateNbt().getList("autographs", NbtCompound.COMPOUND_TYPE).forEach(element -> {
				NbtCompound compound = (NbtCompound) element;
				String name = compound.getString("name");
				FrozenColorizer pigment = FrozenColorizer.fromNBT(compound.getCompound("pigment"));
				MutableText output = Text.literal("");
				int steps = name.length();
				for (int i = 0; i < steps; i++) {
					int color = pigment.getColor(ClientStorage.time * 3, new Vec3d(0, i, 0));
					output.append(Text.literal(String.valueOf(name.charAt(i))).styled(style -> style.withColor(color)));
				}
				cir.getReturnValue().add(output);
			});
		}
	}
}