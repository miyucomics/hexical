package miyucomics.hexical.inits

import miyucomics.hexical.features.player.fields.EvocationField
import miyucomics.hexical.features.player.fields.KeybindField
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import org.lwjgl.glfw.GLFW

object HexicalKeybinds {
	@JvmField
	val OPEN_HEXBOOK = KeyBinding("key.hexical.open_hexbook", GLFW.GLFW_KEY_N, "key.categories.hexical")
	val TELEPATHY_KEYBIND = KeyBinding("key.hexical.telepathy", GLFW.GLFW_KEY_G, "key.categories.hexical")
	private val EVOKE_KEYBIND = KeyBinding("key.hexical.evoke", GLFW.GLFW_KEY_R, "key.categories.hexical")
	private var previousState = mutableMapOf<String, Boolean>()

	fun clientInit() {
		KeyBindingHelper.registerKeyBinding(OPEN_HEXBOOK)
		KeyBindingHelper.registerKeyBinding(EVOKE_KEYBIND)
		KeyBindingHelper.registerKeyBinding(TELEPATHY_KEYBIND)

		ClientTickEvents.END_CLIENT_TICK.register { client: MinecraftClient ->
			if (client.player == null)
				return@register

			if (previousState.keys.contains(EVOKE_KEYBIND.translationKey)) {
				if (previousState[EVOKE_KEYBIND.translationKey] == true && !EVOKE_KEYBIND.isPressed) {
					ClientPlayNetworking.send(EvocationField.END_EVOKING_CHANNEL, PacketByteBufs.empty())
				} else if (previousState[EVOKE_KEYBIND.translationKey] == false && EVOKE_KEYBIND.isPressed) {
					ClientPlayNetworking.send(EvocationField.START_EVOKE_CHANNEL, PacketByteBufs.empty())
				}
			}

			for (key in listOf(client.options.forwardKey, client.options.leftKey, client.options.rightKey, client.options.backKey, client.options.jumpKey, client.options.sneakKey, client.options.useKey, client.options.attackKey, TELEPATHY_KEYBIND, EVOKE_KEYBIND)) {
				if (previousState.keys.contains(key.translationKey)) {
					if (previousState[key.translationKey] == true && !key.isPressed) {
						ClientPlayNetworking.send(KeybindField.RELEASED_KEY_CHANNEL, PacketByteBufs.create().also { it.writeString(key.translationKey) })
					} else if (previousState[key.translationKey] == false && key.isPressed) {
						ClientPlayNetworking.send(KeybindField.PRESSED_KEY_CHANNEL, PacketByteBufs.create().also { it.writeString(key.translationKey) })
					}
				}

				previousState[key.translationKey] = key.isPressed
			}
		}
	}
}