package miyucomics.hexical.inits

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import org.lwjgl.glfw.GLFW

object HexicalKeybinds {
	@JvmField
	val OPEN_HEXBOOK = KeyBinding("key.hexical.open_hexbook", GLFW.GLFW_KEY_N, "key.categories.hexical")
	val TELEPATHY_KEYBIND = KeyBinding("key.hexical.telepathy", GLFW.GLFW_KEY_G, "key.categories.hexical")
	val EVOKE_KEYBIND = KeyBinding("key.hexical.evoke", GLFW.GLFW_KEY_R, "key.categories.hexical")

	fun clientInit() {
		KeyBindingHelper.registerKeyBinding(OPEN_HEXBOOK)
		KeyBindingHelper.registerKeyBinding(EVOKE_KEYBIND)
		KeyBindingHelper.registerKeyBinding(TELEPATHY_KEYBIND)
	}
}