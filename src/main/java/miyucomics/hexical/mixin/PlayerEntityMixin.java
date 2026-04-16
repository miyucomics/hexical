package miyucomics.hexical.mixin;

import miyucomics.hexical.feature.player_state.HexicalPlayerState;
import miyucomics.hexical.feature.player_state.PlayerEntityMinterface;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
@SuppressWarnings("AddedMixinMembersNamePattern")
public class PlayerEntityMixin implements PlayerEntityMinterface {
	@Unique
	private final HexicalPlayerState hexicalPlayerState = new HexicalPlayerState();

	@Inject(method = "tick", at = @At("TAIL"))
	void tick(CallbackInfo ci) {
		hexicalPlayerState.tick((PlayerEntity) (Object) this);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
	void reaadPlayerData(NbtCompound nbt, CallbackInfo ci) {
		hexicalPlayerState.readNbt(nbt);
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
	void writePlayerData(NbtCompound nbt, CallbackInfo ci) {
		hexicalPlayerState.writeNbt(nbt);
	}

	@Override
	public @NotNull HexicalPlayerState getHexicalPlayerState() {
		return hexicalPlayerState;
	}
}