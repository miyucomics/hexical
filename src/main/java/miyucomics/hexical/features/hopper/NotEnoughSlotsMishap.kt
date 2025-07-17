package miyucomics.hexical.features.hopper

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import miyucomics.hexical.HexicalMain
import net.minecraft.text.Text
import net.minecraft.util.DyeColor

class NotEnoughSlotsMishap(val iota: Iota, val attemptedSlot: Int) : Mishap() {
	override fun accentColor(env: CastingEnvironment, errorCtx: Context): FrozenPigment = dyeColor(DyeColor.RED)
	override fun particleSpray(env: CastingEnvironment) = ParticleSpray.Companion.burst(env.mishapSprayPos(), 1.0)
	override fun errorMessage(env: CastingEnvironment, errorCtx: Context): Text = error(HexicalMain.Companion.MOD_ID + ":not_enough_slots", attemptedSlot, iota.display())
	override fun execute(env: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {}
}