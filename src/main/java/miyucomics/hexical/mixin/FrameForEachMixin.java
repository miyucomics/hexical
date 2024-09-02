package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.spell.SpellList;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.api.spell.casting.eval.FrameForEach;
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation;
import at.petrak.hexcasting.api.spell.iota.Iota;
import kotlin.Pair;
import miyucomics.hexical.casting.patterns.eval.OpSisyphus;
import miyucomics.hexical.casting.patterns.eval.OpThemis;
import miyucomics.hexical.enums.InjectedGambit;
import miyucomics.hexical.interfaces.FrameForEachMinterface;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = FrameForEach.class, remap = false)
public abstract class FrameForEachMixin implements FrameForEachMinterface {
	@Unique
	private InjectedGambit hexical$injectedGambit = InjectedGambit.NONE;

	@Shadow
	public abstract SpellList getData();

	@Shadow
	public abstract SpellList getCode();

	@Shadow
	public abstract List<Iota> getBaseStack();

	@Shadow
	public abstract List<Iota> getAcc();

	@Inject(method = "breakDownwards", at = @At("HEAD"), cancellable = true)
	void hijackBreaking(List<? extends Iota> stack, CallbackInfoReturnable<Pair<Boolean, List<Iota>>> cir) {
		if (hexical$injectedGambit == InjectedGambit.NONE)
			return;
		if (hexical$injectedGambit == InjectedGambit.SISYPHUS)
			cir.setReturnValue(OpSisyphus.breakDownwards(getBaseStack()));
		if (hexical$injectedGambit == InjectedGambit.THEMIS)
			cir.setReturnValue(OpThemis.breakDownwards(getBaseStack(), getAcc()));
	}

	@Inject(method = "evaluate", at = @At("HEAD"), cancellable = true)
	void hijackEvaluate(SpellContinuation continuation, ServerWorld level, CastingHarness harness, CallbackInfoReturnable<CastingHarness.CastResult> cir) {
		if (hexical$injectedGambit == InjectedGambit.NONE)
			return;
		if (hexical$injectedGambit == InjectedGambit.SISYPHUS)
			cir.setReturnValue(OpSisyphus.evaluate(continuation, harness, getCode(), getBaseStack()));
		if (hexical$injectedGambit == InjectedGambit.THEMIS)
			cir.setReturnValue(OpThemis.evaluate(continuation, harness, getData(), getCode(), getBaseStack(), getAcc()));
	}

	@Override
	public void overwrite(@NotNull InjectedGambit gambit) {
		hexical$injectedGambit = gambit;
	}

	@Override
	public InjectedGambit getInjectedGambit() {
		return hexical$injectedGambit;
	}
}