package miyucomics.hexical.mixin;

import miyucomics.hexical.interfaces.MinecraftClientMinterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
	@Shadow @Final private MinecraftClient client;

	@Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
	private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
		if (this.client.currentScreen != null || this.client.getOverlay() != null)
			return;
		if (client.player == null || client.player.isSpectator())
			return;
		if (getTweakedItem(client.player) != null && action == 1) {
			if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				((MinecraftClientMinterface) client).leftClick();
				ci.cancel();
			} else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
				((MinecraftClientMinterface) client).rightClick();
				ci.cancel();
			}
		}
	}

	@Unique
	private static Hand getTweakedItem(PlayerEntity player) {
		if (isStackTweaked(player.getStackInHand(Hand.OFF_HAND)))
			return Hand.OFF_HAND;
		if (isStackTweaked(player.getStackInHand(Hand.MAIN_HAND)))
			return Hand.MAIN_HAND;
		return null;
	}

	@Unique
	private static boolean isStackTweaked(ItemStack stack) {
		return stack.hasNbt() && stack.getNbt().contains("hex_tweak");
	}
}