package miyucomics.hexical.status_effects

import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory

class MediaSicknessStatusEffect : StatusEffect(StatusEffectCategory.NEUTRAL, 0x98D982) {
	override fun canApplyUpdateEffect(duration: Int, amplifier: Int) = true
}