package miyucomics.hexical.casting.mishaps

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import miyucomics.hexical.HexicalMain
import net.minecraft.text.Text
import net.minecraft.util.DyeColor

class NeedsChorusFruitMishap : Mishap() {
	override fun accentColor(ctx: CastingEnvironment, errorCtx: Context): FrozenPigment = dyeColor(DyeColor.PURPLE)
	override fun particleSpray(ctx: CastingEnvironment) = ParticleSpray.burst(ctx.caster.pos, 1.0)
	override fun errorMessage(ctx: CastingEnvironment, errorCtx: Context): Text = error(HexicalMain.MOD_ID + ":needs_chorus_fruit")
	override fun execute(ctx: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {}
}