package miyucomics.hexical.mixin;

import miyucomics.hexical.interfaces.PlayerEntityMinterface;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityMinterface {
	@Unique
	private boolean hexical$archLampCastedThisTick = false;

	public boolean getArchLampCastedThisTick() {
		return hexical$archLampCastedThisTick;
	}

	@Override
	public void lampCastedThisTick() {
		hexical$archLampCastedThisTick = true;
	}

	@Inject(method = "tick", at = @At("TAIL"))
	void tick(CallbackInfo ci) {
		hexical$archLampCastedThisTick = false;
	}
}