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

@Mixin(value = Mouse.class, remap = false)
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
		String key = switch (button) {
			case GLFW.GLFW_MOUSE_BUTTON_LEFT -> sneaking ? "left_sneak" : "left";
			case GLFW.GLFW_MOUSE_BUTTON_RIGHT -> sneaking ? "right_sneak" : "right";
			default -> null;
		};

		if (key == null)
			return;

		if (CharmedItemUtilities.getBoolean(stack, key)) {
			int inputMethod = switch (key) {
				case "left" -> 0;
				case "left_sneak" -> 1;
				case "right" -> 2;
				case "right_sneak" -> 3;
				default -> -1;
			};
			sendMessage(hand, inputMethod);
			ci.cancel();
		}
	}

	private void sendMessage(Hand hand, int inputMethod) {
		client.player.swingHand(hand);
		client.player.getWorld().playSound(client.player, client.player.getX(), client.player.getY(), client.player.getZ(), HexSounds.CAST_HERMES, SoundCategory.PLAYERS, 1f, 1f);
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(inputMethod);
		ClientPlayNetworking.send(HexicalNetworking.CHARMED_ITEM_USE_CHANNEL, buf);
	}
}