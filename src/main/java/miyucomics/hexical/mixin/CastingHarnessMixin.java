package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.ExecutionClientView;
import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.HexSounds;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import miyucomics.hexical.inits.HexicalItems;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static miyucomics.hexical.items.GrimoireItemKt.grimoireLookup;

@Mixin(value = CastingVM.class, priority = 900)
public class CastingHarnessMixin {
	@Unique
	private final CastingVM vm = (CastingVM) (Object) this;

	@Inject(method = "queueExecuteAndWrapIota", at = @At("HEAD"), cancellable = true, remap = false)
	private void expandGrimoire(Iota iota, ServerWorld world, CallbackInfoReturnable<ExecutionClientView> cir) {
		CastingEnvironment env = vm.getEnv();
		if (!(env instanceof StaffCastEnv))
			return;

		if (!vm.getImage().getEscapeNext() && iota.getType() == HexIotaTypes.PATTERN) {
			HexPattern pattern = ((PatternIota) iota).getPattern();
			ItemStack grimoire = env.queryForMatchingStack(item -> item.isOf(HexicalItems.GRIMOIRE_ITEM));
			if (grimoire == null)
				return;
			List<Iota> expansion = grimoireLookup((ServerPlayerEntity) env.getCastingEntity(), pattern, grimoire);
			if (expansion != null) {
				env.getCastingEntity().playSound(HexSounds.CAST_HERMES, 0.25f, 1.25f);
				cir.setReturnValue(vm.queueExecuteAndWrapIotas(expansion, world));
			}
		}
	}
}