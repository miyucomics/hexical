package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.ExecutionClientView;
import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import miyucomics.hexical.inits.HexicalItems;
import miyucomics.hexical.items.GrimoireItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(CastingVM.class)
public abstract class CastingVMMixin {
	@Inject(method = "queueExecuteAndWrapIota", at = @At("HEAD"), cancellable = true)
	void expandMacros(Iota iota, ServerWorld world, CallbackInfoReturnable<ExecutionClientView> cir) {
		CastingVM vm = (CastingVM) (Object) this;
		CastingEnvironment env = vm.getEnv();

		if (!(env instanceof StaffCastEnv))
			return;

		var image = vm.getImage();
		if (image.getEscapeNext() || iota.getType() != HexIotaTypes.PATTERN)
			return;

		HexPattern pattern = ((PatternIota) iota).getPattern();
		ItemStack grimoire = env.queryForMatchingStack(stack -> stack.isOf(HexicalItems.GRIMOIRE_ITEM) && stack.getOrCreateNbt().getCompound("expansions").contains(pattern.anglesSignature()));
		if (grimoire == null)
			return;

		List<Iota> expansion = GrimoireItem.getPatternInGrimoire(grimoire, pattern, env.getWorld());
		if (expansion == null)
			return;

		var state = vm.queueExecuteAndWrapIotas(expansion, world);
		state = state.copy(state.isStackClear(), state.getResolutionType(), state.getStackDescs(), state.getRavenmind());
		cir.setReturnValue(state);
	}
}