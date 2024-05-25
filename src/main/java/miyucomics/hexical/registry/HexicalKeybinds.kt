package miyucomics.hexical.registry

import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.network.PacketByteBuf
import org.lwjgl.glfw.GLFW

object HexicalKeybinds {
	private val TELEPATHY_KEYBIND = KeyBinding("key.hexical.telepathy", GLFW.GLFW_KEY_G, "key.categories.hexical")
	private var previouslyHeld = false

	private var states = mutableMapOf<String, Boolean>()

	@JvmStatic
	fun init() {
		KeyBindingHelper.registerKeyBinding(TELEPATHY_KEYBIND)
		ClientTickEvents.END_CLIENT_TICK.register { client: MinecraftClient ->
			for (key in listOf(client.options.forwardKey, client.options.leftKey, client.options.rightKey, client.options.backKey)) {
				if (states.keys.contains(key.translationKey)) {
					if (states[key.translationKey] == true && !key.isPressed) {
						val buf = PacketByteBufs.create()
						buf.writeString(key.translationKey)
						ClientPlayNetworking.send(HexicalNetworking.RELEASED_KEY_PACKET, buf)
					} else if (states[key.translationKey] == false && key.isPressed) {
						val buf = PacketByteBufs.create()
						buf.writeString(key.translationKey)
						ClientPlayNetworking.send(HexicalNetworking.PRESSED_KEY_PACKET, buf)
					}
				}
				states[key.translationKey] = key.isPressed
			}

			if (client.player == null)
				return@register
			if (TELEPATHY_KEYBIND.isPressed) {
				if (!previouslyHeld)
					ClientPlayNetworking.send(HexicalNetworking.START_TELEPATHY_PACKET, PacketByteBuf(Unpooled.buffer()))
				previouslyHeld = true
			} else if (previouslyHeld) {
				ClientPlayNetworking.send(HexicalNetworking.STOP_TELEPATHY_PACKET, PacketByteBuf(Unpooled.buffer()))
				previouslyHeld = false
			}
		}
	}
}