package miyucomics.hexical.casting.mishaps

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import miyucomics.hexical.HexicalMain
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.math.Vec3d

class DyeableMishap(val position: Vec3d) : Mishap() {
	override fun accentColor(ctx: CastingEnvironment, errorCtx: Context): FrozenPigment = dyeColor(DyeColor.YELLOW)
	override fun particleSpray(ctx: CastingEnvironment) = ParticleSpray.burst(position, 1.0)
	override fun errorMessage(ctx: CastingEnvironment, errorCtx: Context): Text = error(HexicalMain.MOD_ID + ":can_not_dye")
	override fun execute(ctx: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {}
}