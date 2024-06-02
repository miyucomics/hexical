package miyucomics.hexical.casting.mishaps

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.Mishap
import miyucomics.hexical.HexicalMain
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.text.Text
import net.minecraft.util.DyeColor

class NoSentinelMishap : Mishap() {
	override fun accentColor(ctx: CastingContext, errorCtx: Context): FrozenColorizer = dyeColor(DyeColor.BLACK)
	override fun particleSpray(ctx: CastingContext) = ParticleSpray.burst(ctx.caster.eyePos, 1.0)
	override fun errorMessage(ctx: CastingContext, errorCtx: Context): Text = error(HexicalMain.MOD_ID + ":no_sentinel")

	override fun execute(ctx: CastingContext, errorCtx: Context, stack: MutableList<Iota>) {
		ctx.caster.addStatusEffect(StatusEffectInstance(StatusEffects.BLINDNESS, 5, 0, false, false, false))
	}
}