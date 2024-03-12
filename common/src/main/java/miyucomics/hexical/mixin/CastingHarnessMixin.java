package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.api.misc.DiscoveryHandlers;
import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.api.spell.casting.ControllerInfo;
import at.petrak.hexcasting.api.spell.casting.sideeffects.OperatorSideEffect;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.PatternIota;
import at.petrak.hexcasting.api.spell.math.HexDir;
import at.petrak.hexcasting.api.spell.math.HexPattern;
import at.petrak.hexcasting.common.lib.HexSounds;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import miyucomics.hexical.interfaces.CastingContextMixinInterface;
import miyucomics.hexical.items.ArchLampItem;
import miyucomics.hexical.utils.CastingUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(value = CastingHarness.class, priority = 900)
public class CastingHarnessMixin {
	@Unique private final CastingHarness hexical$harness = (CastingHarness) (Object) this;

	@WrapWithCondition(method = "updateWithPattern", at = @At(value="INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
	private boolean stopLampParticles (List<OperatorSideEffect> sideEffects, Object effect) {
		return !((CastingContextMixinInterface) (Object) hexical$harness.getCtx()).getCastByLamp();
	}

	@WrapWithCondition(method = "executeIotas", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
	private boolean silenceLamp (ServerWorld world, PlayerEntity player, double x, double y, double z, SoundEvent event, SoundCategory type, float volume, float pitch) {
		return !((CastingContextMixinInterface) (Object) hexical$harness.getCtx()).getCastByLamp();
	}

	@Inject(method = "withdrawMedia", at = @At("HEAD"), cancellable = true, remap = false)
	private void withdrawMediaWisp(int mediaCost, boolean allowOvercast, CallbackInfoReturnable<Integer> cir) {
		CastingContext ctx = hexical$harness.getCtx();
		if (((CastingContextMixinInterface) (Object) ctx).getArchLamp()) {
			if (ctx.getCaster().isCreative()) {
				cir.setReturnValue(0);
				return;
			}
			for (ItemStack stack : ctx.getCaster().getInventory().main) {
				if (stack.getItem() instanceof ArchLampItem && stack.getOrCreateNbt().getBoolean("active")) {
					ADMediaHolder mediaHolder = IXplatAbstractions.INSTANCE.findMediaHolder(stack);
					assert mediaHolder != null;
					int mediaToTake = Math.min(mediaCost, mediaHolder.withdrawMedia(-1, true));
					mediaCost -= mediaToTake;
					mediaHolder.withdrawMedia(mediaToTake, false);
					cir.setReturnValue(mediaCost);
					return;
				}
			}
		}
	}

	@Inject(method = "executeIota", at = @At("HEAD"), cancellable = true, remap = false)
	private void expandGrimoire (Iota iota, ServerWorld world, CallbackInfoReturnable<ControllerInfo> cir) {
		CastingContext ctx = hexical$harness.getCtx();
		if (ctx.getSpellCircle() != null)
			return;
		if (!hexical$harness.getEscapeNext() && iota.getType() == HexIotaTypes.PATTERN && !((PatternIota) iota).getPattern().sigsEqual(HexPattern.fromAngles("qqqaw", HexDir.EAST))) {
			HexPattern pattern = ((PatternIota) iota).getPattern();
			List<Iota> lookupResult = CastingUtils.Companion.grimoireLookup(ctx.getCaster(), pattern, DiscoveryHandlers.collectItemSlots(ctx));
			if (lookupResult != null) {
				ctx.getCaster().playSound(HexSounds.CAST_HERMES, SoundCategory.MASTER, 0.25f, 1.25f);
				cir.setReturnValue(hexical$harness.executeIotas(lookupResult, world));
			}
		}
	}
}