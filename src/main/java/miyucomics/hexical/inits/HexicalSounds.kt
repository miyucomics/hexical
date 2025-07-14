package miyucomics.hexical.inits

import miyucomics.hexical.HexicalMain
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent

object HexicalSounds {
	val AMETHYST_MELT: SoundEvent = register("amethyst_melt")
	val ITEM_DUNKS: SoundEvent = register("item_dunks")
	val EVOKING_MURMUR: SoundEvent = register("evoking_murmur")
	val EVOKING_CAST: SoundEvent = register("evoking_casts")
	val LAMP_ACTIVATE: SoundEvent = register("lamp_activate")
	val LAMP_DEACTIVATE: SoundEvent = register("lamp_deactivate")
	@JvmField
	val SUDDEN_REALIZATION: SoundEvent = register("sudden_realization")
	val REPLENISH_AIR: SoundEvent = register("replenish_air")
	val SCARAB_CHIRPS: SoundEvent = register("scarab_chirps")

	fun init() {}

	private fun register(name: String): SoundEvent {
		val id = HexicalMain.id(name)
		val event = SoundEvent.of(id)
		Registry.register(Registries.SOUND_EVENT, id, event)
		return event
	}
}