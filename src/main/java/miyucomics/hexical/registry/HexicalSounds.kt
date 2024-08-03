package miyucomics.hexical.registry

import miyucomics.hexical.HexicalMain
import net.minecraft.sound.SoundEvent
import net.minecraft.util.registry.Registry

object HexicalSounds {
	lateinit var EVOKING_MURMUR_EVENT: SoundEvent
	lateinit var EVOKING_CAST_EVENT: SoundEvent
	lateinit var LAMP_ACTIVATE_SOUND_EVENT: SoundEvent
	lateinit var LAMP_DEACTIVATE_SOUND_EVENT: SoundEvent
	lateinit var SUDDEN_REALIZATION_EVENT: SoundEvent
	lateinit var PLAYER_SLURP: SoundEvent
	lateinit var CANDLE_FLARES: SoundEvent

	@JvmStatic
	fun init() {
		EVOKING_MURMUR_EVENT = register("evoking_murmur")
		EVOKING_CAST_EVENT = register("evoking_casts")
		LAMP_ACTIVATE_SOUND_EVENT = register("lamp_activate")
		LAMP_DEACTIVATE_SOUND_EVENT = register("lamp_deactivate")
		SUDDEN_REALIZATION_EVENT = register("sudden_realization")
		PLAYER_SLURP = register("player_slurp")
		CANDLE_FLARES = register("candle_flares")
	}

	private fun register(name: String): SoundEvent {
		val id = HexicalMain.id(name)
		val event = SoundEvent(id)
		Registry.register(Registry.SOUND_EVENT, id, event)
		return event
	}
}