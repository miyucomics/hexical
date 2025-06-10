package miyucomics.hexical.mixin;

import miyucomics.hexical.casting.patterns.evocation.OpSetEvocation;
import miyucomics.hexical.data.ArchLampState;
import miyucomics.hexical.data.EvokeState;
import miyucomics.hexical.data.LesserSentinelState;
import miyucomics.hexical.interfaces.PlayerEntityMinterface;
import miyucomics.hexical.utils.CastingUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityMinterface {
	@Unique
	private boolean hexical$archLampCastedThisTick = false;
	@Unique
	private ArchLampState hexical$archLampState = new ArchLampState();
	@Unique
	private NbtCompound hexical$evocation = new NbtCompound();
	@Unique
	private ItemStack hexical$wristpocket = ItemStack.EMPTY;
	@Unique
	private LesserSentinelState hexical$lesserSentinels = new LesserSentinelState();

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
	void reaadPlayerData(NbtCompound compound, CallbackInfo ci) {
		if (compound.contains("arch_lamp"))
			hexical$archLampState = ArchLampState.createFromNbt(compound.getCompound("arch_lamp"));

		if (compound.contains("evocation"))
			hexical$evocation = compound.getCompound("evocation");

		if (compound.contains("lesser_sentinels"))
			hexical$lesserSentinels = LesserSentinelState.createFromNbt(compound.getList("lesser_sentinels", NbtElement.COMPOUND_TYPE));

		hexical$wristpocket = ItemStack.fromNbt(compound.getCompound("wristpocket"));
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
	void writePlayerData(NbtCompound nbtCompound, CallbackInfo ci) {
		nbtCompound.put("arch_lamp", hexical$archLampState.toNbt());
		nbtCompound.put("lesser_sentinels", hexical$lesserSentinels.toNbt());

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
	public @NotNull ArchLampState getArchLampState() {
		return hexical$archLampState;
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
	public @NotNull NbtCompound getEvocation() {
		return hexical$evocation;
	}

	@Override
	public void setEvocation(@NotNull NbtCompound hex) {
		hexical$evocation = hex;
	}

	@Override
	public @NotNull LesserSentinelState getLesserSentinels() {
		return hexical$lesserSentinels;
	}

	@Override
	public void setLesserSentinels(@NotNull LesserSentinelState state) {
		hexical$lesserSentinels = state;
	}
}