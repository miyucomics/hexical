package miyucomics.hexical.casting.mishaps

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.Mishap
import miyucomics.hexical.Hexical
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.Hand

class GrimoireAccessDeniedMishap : Mishap() {
	override fun accentColor(ctx: CastingContext, errorCtx: Context): FrozenColorizer = dyeColor(DyeColor.BLUE)
	override fun particleSpray(ctx: CastingContext) = ParticleSpray.burst(ctx.caster.pos, 1.0)
	override fun errorMessage(ctx: CastingContext, errorCtx: Context): Text = error(Hexical.MOD_ID + ":grimoire_access_denied")

	override fun execute(ctx: CastingContext, errorCtx: Context, stack: MutableList<Iota>) {
		yeetHeldItem(ctx, ctx.otherHand)
	}
}