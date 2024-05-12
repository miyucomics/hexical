package miyucomics.hexical.registry

import dev.architectury.event.events.client.ClientTickEvent
import dev.architectury.networking.NetworkManager
import dev.architectury.registry.client.keymappings.KeyMappingRegistry
import io.netty.buffer.Unpooled
import miyucomics.hexical.Hexical
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.network.PacketByteBuf
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW

object HexicalKeybinds {
	private val TELEPATHY_KEYBIND = KeyBinding("key.hexical.telepathy", GLFW.GLFW_KEY_G, "key.categories.hexical")
	private var previouslyHeld = false

	private var states = mutableMapOf<String, Boolean>()

	@JvmStatic
	fun init() {
		KeyMappingRegistry.register(TELEPATHY_KEYBIND)
		ClientTickEvent.CLIENT_POST.register(ClientTickEvent.Client { client: MinecraftClient ->
			for (key in listOf(client.options.forwardKey, client.options.leftKey, client.options.rightKey, client.options.backKey)) {
				if (states.keys.contains(key.translationKey)) {
					if (states[key.translationKey] == true && !key.isPressed) {
						val buf = PacketByteBuf(Unpooled.buffer())
						buf.writeString(key.translationKey)
						NetworkManager.sendToServer(Hexical.RELEASED_KEY, buf)
					} else if (states[key.translationKey] == false && key.isPressed) {
						val buf = PacketByteBuf(Unpooled.buffer())
						buf.writeString(key.translationKey)
						NetworkManager.sendToServer(Hexical.PRESSED_KEY, buf)
					}
				}
				states[key.translationKey] = key.isPressed
			}

			if (client.player == null)
				return@Client
			if (TELEPATHY_KEYBIND.isPressed) {
				if (!previouslyHeld)
					NetworkManager.sendToServer(Hexical.START_TELEPATHY_PACKET, PacketByteBuf(Unpooled.buffer()))
				previouslyHeld = true
			} else if (previouslyHeld) {
				NetworkManager.sendToServer(Hexical.STOP_TELEPATHY_PACKET, PacketByteBuf(Unpooled.buffer()))
				previouslyHeld = false
			}
		})
	}
}