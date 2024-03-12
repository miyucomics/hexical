package miyucomics.hexical.registry

import dev.architectury.event.events.client.ClientTickEvent
import dev.architectury.registry.client.keymappings.KeyMappingRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW

object HexicalKeybinds {
	private val TELEPATHY_KEYBIND = KeyBinding("key.hexical.telepathy", GLFW.GLFW_KEY_G, "key.categories.hexical")

	@JvmStatic
	fun init() {
		KeyMappingRegistry.register(TELEPATHY_KEYBIND);
		ClientTickEvent.CLIENT_POST.register(ClientTickEvent.Client { client: MinecraftClient ->
			if (client.player == null)
				return@Client
			while (TELEPATHY_KEYBIND.wasPressed())
				client.player!!.sendMessage(Text.literal("using mind powers!!"), false)
		})
	}
}