package miyucomics.hexical.registry

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.status_effects.MediaVisionStatusEffect
import net.minecraft.util.registry.Registry

object HexicalStatusEffects {
	val MEDIA_VISION_STATUS_EFFECT = MediaVisionStatusEffect()

	@JvmStatic
	fun init() {
		Registry.register(Registry.STATUS_EFFECT, HexicalMain.id("media_vision"), MEDIA_VISION_STATUS_EFFECT)
	}
}