package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.GarbageIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.Vec3Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem;
import at.petrak.hexcasting.common.casting.actions.rw.OpRead;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import miyucomics.hexical.inits.HexicalItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = OpRead.class, remap = false)
public class OpReadMixin {
	@WrapMethod(method = "execute")
	private List<Iota> readFromCompassCurio(List<Iota> args, CastingEnvironment env, Operation<List<Iota>> original) throws MishapBadOffhandItem {
		try {
			return original.call(args, env);
		} catch (Throwable exception) { // Java does not know how to handle Kotlin not needing to specify that it throws something, so we just need to be general and just use Throwable
			CastingEnvironment.HeldItemInfo data = env.getHeldItemToOperateOn(
				item -> item.isOf(HexicalItems.CURIO_COMPASS)
					&& item.hasNbt()
					&& item.getNbt().contains("needle", NbtElement.INT_ARRAY_TYPE)
					&& item.getNbt().getIntArray("needle").length >= 3
			);
			if (data == null)
				 throw MishapBadOffhandItem.of(null, "iota.read");

			LivingEntity caster = env.getCastingEntity();
			if (caster == null)
				return List.of(new GarbageIota());

			int[] coordinates = data.stack().getNbt().getIntArray("needle");
			return List.of(new Vec3Iota(new Vec3d(coordinates[0], coordinates[1], coordinates[2]).subtract(caster.getEyePos()).normalize()));
		}
	}

	@Inject(method = "execute", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/addldata/ADIotaHolder;readIota(Lnet/minecraft/server/world/ServerWorld;)Lat/petrak/hexcasting/api/casting/iota/Iota;"), remap = true)
	private static void dontEatTwoHexbursts(List<? extends Iota> args, CastingEnvironment env, CallbackInfoReturnable<List<Iota>> cir, @Local ItemStack handStack) {
		if (handStack.isOf(HexicalItems.HEXBURST_ITEM))
			handStack.increment(1);
	}
}