package miyucomics.hexical.casting.mishaps

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import miyucomics.hexical.HexicalMain
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.math.Vec3d

class AssertMishap(val iota: Iota) : Mishap() {
	override fun accentColor(env: CastingEnvironment, errorCtx: Context): FrozenPigment = dyeColor(DyeColor.PURPLE)
	override fun errorMessage(env: CastingEnvironment, errorCtx: Context): Text = error(HexicalMain.MOD_ID + ":assert", iota.display())
	override fun execute(env: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {}
}