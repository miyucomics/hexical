package miyucomics.hexical.registry

import miyucomics.hexical.HexicalMain
import net.minecraft.sound.SoundEvent
import net.minecraft.util.registry.Registry

object HexicalSounds {
	lateinit var EVOKING_MURMUR: SoundEvent
	lateinit var EVOKING_CAST: SoundEvent
	lateinit var LAMP_ACTIVATE: SoundEvent
	lateinit var LAMP_DEACTIVATE: SoundEvent
	lateinit var SUDDEN_REALIZATION: SoundEvent
	lateinit var PLAYER_SLURP: SoundEvent
	lateinit var CANDLE_FLARES: SoundEvent
	lateinit var REPLENISH_AIR: SoundEvent

	@JvmStatic
	fun init() {
		EVOKING_MURMUR = register("evoking_murmur")
		EVOKING_CAST = register("evoking_casts")
		LAMP_ACTIVATE = register("lamp_activate")
		LAMP_DEACTIVATE = register("lamp_deactivate")
		SUDDEN_REALIZATION = register("sudden_realization")
		PLAYER_SLURP = register("player_slurp")
		CANDLE_FLARES = register("candle_flares")
		REPLENISH_AIR = register("replenish_air")
	}

	private fun register(name: String): SoundEvent {
		val id = HexicalMain.id(name)
		val event = SoundEvent(id)
		Registry.register(Registry.SOUND_EVENT, id, event)
		return event
	}
}