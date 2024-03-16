package miyucomics.hexical.registry

import dev.architectury.event.events.client.ClientTickEvent
import dev.architectury.networking.NetworkManager
import dev.architectury.registry.client.keymappings.KeyMappingRegistry
import io.netty.buffer.Unpooled
import miyucomics.hexical.Hexical
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.network.PacketByteBuf
import org.lwjgl.glfw.GLFW

object HexicalKeybinds {
	private val TELEPATHY_KEYBIND = KeyBinding("key.hexical.telepathy", GLFW.GLFW_KEY_G, "key.categories.hexical")
	private var previouslyHeld = false

	@JvmStatic
	fun init() {
		KeyMappingRegistry.register(TELEPATHY_KEYBIND);
		ClientTickEvent.CLIENT_POST.register(ClientTickEvent.Client { client: MinecraftClient ->
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