package miyucomics.hexical.features.prestidigitation

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import miyucomics.hexical.HexicalMain
import net.minecraft.util.DyeColor
import net.minecraft.util.math.Vec3d

class MishapNoPrestidigitation(val position: Vec3d) : Mishap() {
    override fun accentColor(env: CastingEnvironment, errorCtx: Context): FrozenPigment = dyeColor(DyeColor.RED)
    override fun particleSpray(env: CastingEnvironment) = ParticleSpray.burst(position, 1.0)
    override fun errorMessage(env: CastingEnvironment, errorCtx: Context) = error(HexicalMain.MOD_ID + ":no_prestidigitation")
    override fun execute(env: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {}
}