package miyucomics.hexical.inits

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import org.lwjgl.glfw.GLFW

object HexicalKeybinds {
	private val EVOKE_KEYBIND = KeyBinding("key.hexical.evoke", GLFW.GLFW_KEY_R, "key.categories.hexical")
	private val TELEPATHY_KEYBIND = KeyBinding("key.hexical.telepathy", GLFW.GLFW_KEY_G, "key.categories.hexical")
	private var states = mutableMapOf<String, Boolean>()

	@JvmStatic
	fun init() {
		KeyBindingHelper.registerKeyBinding(EVOKE_KEYBIND)
		KeyBindingHelper.registerKeyBinding(TELEPATHY_KEYBIND)

		ClientTickEvents.END_CLIENT_TICK.register { client: MinecraftClient ->
			if (client.player == null)
				return@register

			if (states.keys.contains(EVOKE_KEYBIND.translationKey)) {
				if (states[EVOKE_KEYBIND.translationKey] == true && !EVOKE_KEYBIND.isPressed) {
					ClientPlayNetworking.send(HexicalNetworking.END_EVOKING_CHANNEL, PacketByteBufs.empty())
				} else if (states[EVOKE_KEYBIND.translationKey] == false && EVOKE_KEYBIND.isPressed) {
					ClientPlayNetworking.send(HexicalNetworking.START_EVOKE_CHANNEL, PacketByteBufs.empty())
				}
			}

			for (key in listOf(client.options.forwardKey, client.options.leftKey, client.options.rightKey, client.options.backKey, client.options.jumpKey, TELEPATHY_KEYBIND, EVOKE_KEYBIND)) {
				if (states.keys.contains(key.translationKey)) {
					if (states[key.translationKey] == true && !key.isPressed) {
						val buf = PacketByteBufs.create()
						buf.writeString(key.translationKey)
						ClientPlayNetworking.send(HexicalNetworking.RELEASED_KEY_CHANNEL, buf)
					} else if (states[key.translationKey] == false && key.isPressed) {
						val buf = PacketByteBufs.create()
						buf.writeString(key.translationKey)
						ClientPlayNetworking.send(HexicalNetworking.PRESSED_KEY_CHANNEL, buf)
					}
				}
				states[key.translationKey] = key.isPressed
			}
		}
	}
}