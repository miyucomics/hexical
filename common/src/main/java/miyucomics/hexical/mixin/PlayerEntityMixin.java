package miyucomics.hexical.mixin;

import miyucomics.hexical.interfaces.PlayerEntityMinterface;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityMinterface {
	@Unique
	private static final String WRISTPOCKET_PATH = "hexical:wristpocket";
	@Unique
	private boolean hexical$archLampCastedThisTick = false;
	@Unique
	private ItemStack hexical$wristpocket = ItemStack.EMPTY;

	@Inject(method = "tick", at = @At("TAIL"))
	void tick(CallbackInfo ci) {
		hexical$archLampCastedThisTick = false;
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
	void writeData(NbtCompound nbt, CallbackInfo ci) {
		NbtCompound tag = new NbtCompound();
		hexical$wristpocket.writeNbt(tag);
		nbt.put(WRISTPOCKET_PATH, tag);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
	void readData(NbtCompound nbt, CallbackInfo ci) {
		hexical$wristpocket = ItemStack.fromNbt(nbt.getCompound(WRISTPOCKET_PATH));
	}

	public boolean getArchLampCastedThisTick() {
		return hexical$archLampCastedThisTick;
	}

	@Override
	public void lampCastedThisTick() {
		hexical$archLampCastedThisTick = true;
	}

	@Override
	public void stashWristpocket(@Nullable ItemStack stack) {
		hexical$wristpocket = stack;
	}

	@Nullable
	@Override
	public ItemStack wristpocketItem() {
		return hexical$wristpocket;
	}
}