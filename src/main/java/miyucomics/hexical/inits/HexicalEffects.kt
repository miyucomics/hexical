package miyucomics.hexical.inits

import miyucomics.hexical.HexicalMain
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object HexicalEffects {
	@JvmField
	val WOOLEYED_EFFECT = MediaBloodEffect()

	@JvmStatic
	fun init() {
		Registry.register(Registries.STATUS_EFFECT, HexicalMain.id("wooleyed"), WOOLEYED_EFFECT)
	}
}

class MediaBloodEffect : StatusEffect(StatusEffectCategory.BENEFICIAL, 0xff_a678f1.toInt())