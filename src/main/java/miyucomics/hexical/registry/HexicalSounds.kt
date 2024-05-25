package miyucomics.hexical.registry

import miyucomics.hexical.HexicalMain
import net.minecraft.sound.SoundEvent
import net.minecraft.util.registry.Registry

object HexicalSounds {
	private val LAMP_ACTIVATE_SOUND_ID = HexicalMain.id("lamp_activate")
	val LAMP_ACTIVATE_SOUND_EVENT = SoundEvent(LAMP_ACTIVATE_SOUND_ID)
	private val LAMP_DEACTIVATE_SOUND_ID = HexicalMain.id("lamp_deactivate")
	val LAMP_DEACTIVATE_SOUND_EVENT = SoundEvent(LAMP_DEACTIVATE_SOUND_ID)

	@JvmStatic
	fun init() {
		Registry.register(Registry.SOUND_EVENT, LAMP_ACTIVATE_SOUND_ID, LAMP_ACTIVATE_SOUND_EVENT)
		Registry.register(Registry.SOUND_EVENT, LAMP_DEACTIVATE_SOUND_ID, LAMP_DEACTIVATE_SOUND_EVENT)
	}
}