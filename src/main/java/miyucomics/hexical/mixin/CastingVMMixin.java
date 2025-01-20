package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.ExecutionClientView;
import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import kotlin.collections.CollectionsKt;
import miyucomics.hexical.inits.HexicalItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = CastingVM.class, priority = 100)
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

		NbtCompound data = grimoire.getOrCreateNbt().getCompound("expansions");
		if (!data.contains(pattern.anglesSignature()))
			return;
		Iota deserialized = IotaType.deserialize(data.getCompound(pattern.anglesSignature()), env.getWorld());
		if (!(deserialized instanceof ListIota))
			return;

		cir.setReturnValue(vm.queueExecuteAndWrapIotas(CollectionsKt.toList(((ListIota) deserialized).getList()), world));
	}
}