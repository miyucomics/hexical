package miyucomics.hexical.registry

import dev.architectury.registry.registries.DeferredRegister
import miyucomics.hexical.Hexical
import net.minecraft.sound.SoundEvent
import net.minecraft.util.registry.Registry

object HexicalSounds {
	private val SOUNDS: DeferredRegister<SoundEvent> = DeferredRegister.create(Hexical.MOD_ID, Registry.SOUND_EVENT_KEY)
	var LAMP_ACTIVATE_SOUND_EVENT = register("lamp_activate")
	var LAMP_DEACTIVATE_SOUND_EVENT = register("lamp_deactivate")

	@JvmStatic
	fun init() {
		SOUNDS.register()
	}

	private fun register(name: String): SoundEvent {
		val soundEvent = SoundEvent(Hexical.id(name))
		SOUNDS.register(name) { soundEvent }
		return soundEvent
	}
}