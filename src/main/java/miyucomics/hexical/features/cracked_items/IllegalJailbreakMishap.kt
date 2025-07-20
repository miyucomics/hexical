package miyucomics.hexical.features.cracked_items

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import miyucomics.hexical.HexicalMain
import net.minecraft.text.Text
import net.minecraft.util.DyeColor

class IllegalJailbreakMishap : Mishap() {
	override fun accentColor(env: CastingEnvironment, errorCtx: Context): FrozenPigment = dyeColor(DyeColor.RED)
	override fun particleSpray(env: CastingEnvironment) = ParticleSpray.burst(env.mishapSprayPos(), 1.0)
	override fun errorMessage(env: CastingEnvironment, errorCtx: Context): Text = error(HexicalMain.MOD_ID + ":illegal_jailbreak")
	override fun execute(env: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {
		env.mishapEnvironment.blind(300)
	}
}