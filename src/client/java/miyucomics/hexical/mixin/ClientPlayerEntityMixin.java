package miyucomics.hexical.mixin;

import at.petrak.hexcasting.client.gui.GuiSpellcasting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
	@Shadow
	public Input input;

	@Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onMovement(Lnet/minecraft/client/input/Input;)V"))
	private void onInputUpdate(CallbackInfo info) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.currentScreen instanceof GuiSpellcasting) {
			KeyBinding.updatePressedStates();
			GameOptions keys = client.options;
			input.pressingForward = keys.forwardKey.isPressed();
			input.pressingBack = keys.backKey.isPressed();
			input.pressingLeft = keys.leftKey.isPressed();
			input.pressingRight = keys.rightKey.isPressed();
			input.jumping = keys.jumpKey.isPressed();
			input.sneaking = keys.sneakKey.isPressed();
		}
	}
}