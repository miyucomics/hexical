package miyucomics.hexical.mixin;

import miyucomics.hexical.features.player_state.PlayerState;
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
	private final PlayerState hexicalPlayerState = new PlayerState();

	@Inject(method = "tick", at = @At("TAIL"))
	void tick(CallbackInfo ci) {
		hexicalPlayerState.tick((PlayerEntity) (Object) this);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
	void reaadPlayerData(NbtCompound compound, CallbackInfo ci) {
		hexicalPlayerState.readNbt(compound);
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
	void writePlayerData(NbtCompound compound, CallbackInfo ci) {
		hexicalPlayerState.writeNbt(compound);
	}

	@Override
	public @NotNull PlayerState getPlayerState() {
		return hexicalPlayerState;
	}
}