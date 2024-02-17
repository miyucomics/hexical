package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.api.spell.casting.sideeffects.OperatorSideEffect;
import miyucomics.hexical.items.LampItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = CastingHarness.class, priority = 1001)
public abstract class CastingHarnessMixin {
	@Unique private final CastingHarness harness = (CastingHarness) (Object) this;

	@Redirect(method = "updateWithPattern", at = @At(value="INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), remap = false)
	private boolean stopLampParticles (List<OperatorSideEffect> sideEffects, Object effect) {
		if (effect instanceof OperatorSideEffect.Particles particles) {
			CastingContext ctx = harness.getCtx();
			if (LampItem.isUsingLamp(ctx))
				return false;
			return sideEffects.add(particles);
		}
		return sideEffects.add((OperatorSideEffect) effect);
	}

	@Redirect(method = "executeIotas", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V", remap = true), remap = false)
	private void silenceLamp (ServerWorld world, PlayerEntity player, double x, double y, double z, SoundEvent event, SoundCategory type, float volume, float pitch) {
		CastingContext ctx = harness.getCtx();
		if (!LampItem.isUsingLamp(ctx))
			world.playSound(player, x, y, z, event, type, volume, pitch);
	}
}