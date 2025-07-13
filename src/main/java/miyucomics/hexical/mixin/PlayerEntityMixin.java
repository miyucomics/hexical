package miyucomics.hexical.mixin;

import miyucomics.hexical.features.player.PlayerManager;
import miyucomics.hexical.interfaces.PlayerEntityMinterface;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityMinterface {
	@Unique
	private final PlayerManager hexicalPlayerManager = new PlayerManager();

	@Inject(method = "tick", at = @At("TAIL"))
	void tick(CallbackInfo ci) {
		hexicalPlayerManager.tick((PlayerEntity) (Object) this);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
	void reaadPlayerData(NbtCompound compound, CallbackInfo ci) {
		hexicalPlayerManager.readNbt(compound);
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
	void writePlayerData(NbtCompound compound, CallbackInfo ci) {
		hexicalPlayerManager.writeNbt(compound);
	}

	@Override
	public @NotNull PlayerManager getPlayerManager() {
		return hexicalPlayerManager;
	}
}