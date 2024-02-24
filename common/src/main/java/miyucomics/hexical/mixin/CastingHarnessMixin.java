package miyucomics.hexical.mixin;

import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.api.spell.casting.sideeffects.OperatorSideEffect;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import miyucomics.hexical.interfaces.CastingContextMixinInterface;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(value = CastingHarness.class)
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
}