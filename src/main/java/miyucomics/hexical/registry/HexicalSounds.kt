package miyucomics.hexical.registry

import miyucomics.hexical.HexicalMain
import net.minecraft.sound.SoundEvent
import net.minecraft.util.registry.Registry

object HexicalSounds {
	private val EVOKING_MURMUR_ID = HexicalMain.id("evoking_murmur")
	val EVOKING_MURMUR_EVENT = SoundEvent(EVOKING_MURMUR_ID)
	private val EVOKING_CAST_ID = HexicalMain.id("evoking_casts")
	val EVOKING_CAST_EVENT = SoundEvent(EVOKING_CAST_ID)
	private val LAMP_ACTIVATE_SOUND_ID = HexicalMain.id("lamp_activate")
	val LAMP_ACTIVATE_SOUND_EVENT = SoundEvent(LAMP_ACTIVATE_SOUND_ID)
	private val LAMP_DEACTIVATE_SOUND_ID = HexicalMain.id("lamp_deactivate")
	val LAMP_DEACTIVATE_SOUND_EVENT = SoundEvent(LAMP_DEACTIVATE_SOUND_ID)
	private val SUDDEN_REALIZATION_ID = HexicalMain.id("sudden_realization")
	val SUDDEN_REALIZATION_EVENT = SoundEvent(SUDDEN_REALIZATION_ID)

	@JvmStatic
	fun init() {
		Registry.register(Registry.SOUND_EVENT, EVOKING_MURMUR_ID, EVOKING_MURMUR_EVENT)
		Registry.register(Registry.SOUND_EVENT, EVOKING_CAST_ID, EVOKING_CAST_EVENT)
		Registry.register(Registry.SOUND_EVENT, LAMP_ACTIVATE_SOUND_ID, LAMP_ACTIVATE_SOUND_EVENT)
		Registry.register(Registry.SOUND_EVENT, LAMP_DEACTIVATE_SOUND_ID, LAMP_DEACTIVATE_SOUND_EVENT)
		Registry.register(Registry.SOUND_EVENT, SUDDEN_REALIZATION_ID, SUDDEN_REALIZATION_EVENT)
	}
}