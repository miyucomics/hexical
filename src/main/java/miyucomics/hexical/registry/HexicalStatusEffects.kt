package miyucomics.hexical.registry

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.status_effects.MediaSicknessStatusEffect
import net.minecraft.util.registry.Registry

object HexicalStatusEffects {
	val MEDIA_SICKNESS_STATUS_EFFECT = MediaSicknessStatusEffect()

	@JvmStatic
	fun init() {
		Registry.register(Registry.STATUS_EFFECT, HexicalMain.id("media_sickness"), MEDIA_SICKNESS_STATUS_EFFECT)
	}
}