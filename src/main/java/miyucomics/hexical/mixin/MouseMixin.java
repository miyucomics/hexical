package miyucomics.hexical.mixin;

import at.petrak.hexcasting.common.lib.HexSounds;
import kotlin.Pair;
import miyucomics.hexical.registry.HexicalNetworking;
import miyucomics.hexical.utils.CharmedItemUtilities;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

		Pair<Hand, ItemStack> charmedItem = CharmedItemUtilities.getCharmedItem(client.player);
		if (charmedItem == null) return;

		handleButtonPress(charmedItem.getFirst(), charmedItem.getSecond(), button, client.player.isSneaking(), ci);
	}

	@Unique
	private void handleButtonPress(Hand hand, ItemStack stack, int button, boolean sneaking, CallbackInfo ci) {
		int buttonPressed = switch (button) {
			case GLFW.GLFW_MOUSE_BUTTON_1 -> 1; // left
			case GLFW.GLFW_MOUSE_BUTTON_2 -> 2; // right
			case GLFW.GLFW_MOUSE_BUTTON_3 -> 3; // middle
			case GLFW.GLFW_MOUSE_BUTTON_4 -> 4;
			case GLFW.GLFW_MOUSE_BUTTON_5 -> 5;
			case GLFW.GLFW_MOUSE_BUTTON_6 -> 6;
			case GLFW.GLFW_MOUSE_BUTTON_7 -> 7;
			case GLFW.GLFW_MOUSE_BUTTON_8 -> 8;
			default -> -1;
		};

		if (buttonPressed == -1)
			return;

		if (CharmedItemUtilities.shouldIntercept(stack, buttonPressed, sneaking)) {
			sendMessage(hand, buttonPressed);
			ci.cancel();
		}
	}

	@Unique
	private void sendMessage(Hand hand, int inputMethod) {
		assert client.player != null;
		client.player.swingHand(hand);
		client.player.getWorld().playSound(client.player, client.player.getX(), client.player.getY(), client.player.getZ(), HexSounds.CAST_HERMES, SoundCategory.PLAYERS, 1f, 1f);
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(inputMethod);
		ClientPlayNetworking.send(HexicalNetworking.CHARMED_ITEM_USE_CHANNEL, buf);
	}
}