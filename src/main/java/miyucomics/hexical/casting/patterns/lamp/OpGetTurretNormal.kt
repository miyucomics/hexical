package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexical.casting.env.HandLampCastEnv
import miyucomics.hexical.casting.env.TurretLampCastEnv
import miyucomics.hexical.casting.mishaps.NoHandLampMishap
import miyucomics.hexical.casting.mishaps.NoTurretMishap
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.Vec3d

class OpGetTurretNormal : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env !is TurretLampCastEnv)
			throw NoTurretMishap()
		return Vec3d.of(env.normal).asActionResult
	}
}