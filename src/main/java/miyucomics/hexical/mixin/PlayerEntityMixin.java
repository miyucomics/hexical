package miyucomics.hexical.mixin;

import miyucomics.hexical.casting.patterns.evocation.OpSetEvocation;
import miyucomics.hexical.interfaces.PlayerEntityMinterface;
import miyucomics.hexical.state.EvokeState;
import miyucomics.hexical.utils.CastingUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerEntityMinterface {
	@Unique
	private boolean hexical$archLampCastedThisTick = false;
	@Unique
	private NbtCompound hexical$evocation = new NbtCompound();
	@Unique
	private ItemStack hexical$wristpocket = ItemStack.EMPTY;

	@Inject(method = "tick", at = @At("TAIL"))
	void tick(CallbackInfo ci) {
		PlayerEntity player = ((PlayerEntity) (Object) this);

		if (player.getWorld().isClient)
			return;

		if (EvokeState.isEvoking(player.getUuid()) && CastingUtils.isEnlightened((ServerPlayerEntity) player))
			if (EvokeState.getDuration(player.getUuid()) == 0)
				OpSetEvocation.evoke((ServerPlayerEntity) player);

		hexical$archLampCastedThisTick = false;
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
	void reaadPlayerData(NbtCompound nbtCompound, CallbackInfo ci) {
		if (nbtCompound.contains("evocation"))
			hexical$evocation = nbtCompound.getCompound("evocation");

		hexical$wristpocket = ItemStack.fromNbt(nbtCompound.getCompound("wristpocket"));
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
	void writePlayerData(NbtCompound nbtCompound, CallbackInfo ci) {
		if (hexical$evocation != null)
			nbtCompound.put("evocation", hexical$evocation);

		NbtCompound wristpocket = new NbtCompound();
		hexical$wristpocket.writeNbt(wristpocket);
		nbtCompound.put("wristpocket", wristpocket);
	}

	public boolean getArchLampCastedThisTick() {
		return hexical$archLampCastedThisTick;
	}

	@Override
	public void archLampCasted() {
		hexical$archLampCastedThisTick = true;
	}

	@Override
	public @NotNull ItemStack getWristpocket() {
		return hexical$wristpocket;
	}

	@Override
	public void setWristpocket(@NotNull ItemStack stack) {
		hexical$wristpocket = stack;
	}

	@Override
	public NbtCompound getEvocation() {
		return hexical$evocation;
	}

	@Override
	public void setEvocation(@NotNull NbtCompound hex) {
		hexical$evocation = hex;
	}
}