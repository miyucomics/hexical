package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.NullIota;
import at.petrak.hexcasting.api.spell.iota.Vec3Iota;
import at.petrak.hexcasting.common.casting.operators.rw.OpRead;
import miyucomics.hexical.registry.HexicalItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = OpRead.class, remap = false)
public class OpReadMixin {
	@Inject(method = "execute(Ljava/util/List;Lat/petrak/hexcasting/api/spell/casting/CastingContext;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
	private void readCompass(List<? extends Iota> args, CastingContext ctx, CallbackInfoReturnable<List<Iota>> cir) {
		ItemStack stack = ctx.getHeldItemToOperateOn(item -> item.isOf(HexicalItems.CONJURED_COMPASS_ITEM)).getFirst();
		if (stack.isOf(HexicalItems.CONJURED_COMPASS_ITEM)) {
			if (!stack.getOrCreateNbt().contains("location")) {
				cir.setReturnValue(List.of(new NullIota()));
			} else {
				NbtCompound nbt = stack.getOrCreateNbt();
				cir.setReturnValue(List.of(new Vec3Iota(new Vec3d(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")).subtract(ctx.getCaster().getEyePos()).normalize())));
			}
		}
	}
}