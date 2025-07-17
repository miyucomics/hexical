package miyucomics.hexical.features.lamps

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import net.minecraft.nbt.NbtCompound

class OpGetHandLampData(private val process: (CastingEnvironment, NbtCompound) -> List<Iota>) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env !is HandLampCastEnv)
			throw NeedsHandLampMishap()
		val nbt = env.castingEntity!!.activeItem.nbt ?: return listOf(NullIota())
		return process(env, nbt)
	}
}