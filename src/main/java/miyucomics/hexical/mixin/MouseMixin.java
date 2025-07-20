package miyucomics.hexical.mixin;

import kotlin.Pair;
import miyucomics.hexical.features.charms.CharmUtilities;
import miyucomics.hexical.features.charms.ServerCharmedUseReceiver;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Mouse.class)
public class MouseMixin {
	@Shadow @Final private MinecraftClient client;

	@Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
	private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
		if (client.currentScreen != null || client.getOverlay() != null) return;
		if (client.player == null || client.player.isSpectator()) return;
		if (action != GLFW.GLFW_PRESS) return;

		int buttonPressed = switch (button) {
			case GLFW.GLFW_MOUSE_BUTTON_1 -> 0; // left
			case GLFW.GLFW_MOUSE_BUTTON_2 -> 1; // right
			case GLFW.GLFW_MOUSE_BUTTON_3 -> 2; // middle
			case GLFW.GLFW_MOUSE_BUTTON_4 -> 3;
			case GLFW.GLFW_MOUSE_BUTTON_5 -> 4;
			case GLFW.GLFW_MOUSE_BUTTON_6 -> 5;
			case GLFW.GLFW_MOUSE_BUTTON_7 -> 6;
			case GLFW.GLFW_MOUSE_BUTTON_8 -> 7;
			default -> -1;
		};

		if (buttonPressed == -1)
			return;

		for (Pair<Hand, ItemStack> pair : CharmUtilities.getUseableCharmedItems(client.player)) {
			if (!CharmUtilities.shouldIntercept(pair.getSecond(), buttonPressed, client.player.isSneaking()))
				continue;

			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeInt(buttonPressed);
			buf.writeInt(pair.getFirst().ordinal());
			ClientPlayNetworking.send(ServerCharmedUseReceiver.CHARMED_ITEM_USE_CHANNEL, buf);
			ci.cancel();
			return;
		}
	}
}