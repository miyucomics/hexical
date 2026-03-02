package miyucomics.hexical.mixin;

import miyucomics.hexical.features.periwinkle.SnifferEntityMinterface;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.SnifferEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.entity.passive.SnifferBrain$DiggingTask")
public class SnifferBrainMixin {
	@Inject(method = "shouldKeepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/SnifferEntity;J)Z", at = @At("HEAD"), cancellable = true)
	private void allowCustomDigging(ServerWorld serverWorld, SnifferEntity snifferEntity, long l, CallbackInfoReturnable<Boolean> cir) {
		if (((SnifferEntityMinterface) snifferEntity).isDiggingCustom())
			cir.setReturnValue(true);
	}

	@Inject(method = "finishRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/SnifferEntity;J)V", at = @At("HEAD"), cancellable = true)
	private void forceDiggingSuccess(ServerWorld serverWorld, SnifferEntity snifferEntity, long l, CallbackInfo ci) {
		if (((SnifferEntityMinterface) snifferEntity).isDiggingCustom()) {
			snifferEntity.getBrain().remember(MemoryModuleType.SNIFF_COOLDOWN, Unit.INSTANCE, 9600L);
			ci.cancel();
		}
	}
}