package miyucomics.hexical.mixin;

import at.petrak.hexcasting.common.lib.HexSounds;
import kotlin.Pair;
import miyucomics.hexical.features.charms.CharmUtilities;
import miyucomics.hexical.features.charms.ServerCharmedUseReceiver;
import miyucomics.hexical.features.curios.CurioItem;
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
		if (action != GLFW.GLFW_PRESS && action != GLFW.GLFW_RELEASE) return;
		if (button > GLFW.GLFW_MOUSE_BUTTON_8) return;
		int pressed = button - GLFW.GLFW_MOUSE_BUTTON_1;

		for (Pair<Hand, ItemStack> pair : CharmUtilities.getUseableCharmedItems(client.player)) {
			if (!CharmUtilities.shouldIntercept(pair.getSecond(), pressed, client.player.isSneaking(), action == GLFW.GLFW_RELEASE))
				continue;

			if (!(pair.getSecond().getItem() instanceof CurioItem)) {
				client.player.swingHand(pair.getFirst());
				client.player.clientWorld.playSound(null, client.player.getX(), client.player.getY(), client.player.getZ(), HexSounds.CAST_HERMES, SoundCategory.MASTER, 0.25f, 1f);
			}

			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeInt(pressed);
			buf.writeInt(pair.getFirst().ordinal());
			ClientPlayNetworking.send(ServerCharmedUseReceiver.CHARMED_ITEM_USE_CHANNEL, buf);
			if (action == GLFW.GLFW_PRESS) ci.cancel();
			return;
		}
	}
}