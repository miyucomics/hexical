package miyucomics.hexical.casting.patterns.lamp

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexical.casting.mishaps.NeedsSourceMishap
import miyucomics.hexical.interfaces.CastingEnvironmentMinterface
import net.minecraft.nbt.NbtCompound

class OpGetHandLampData(private val process: (CastingEnvironment, NbtCompound) -> List<Iota>) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if ((env as CastingEnvironmentMinterface).getSpecializedSource() != SpecializedSource.HAND_LAMP)
			throw NeedsSourceMishap("hand_lamp")
		val nbt = env.caster.activeItem.nbt ?: return listOf(NullIota())
		return process(env, nbt)
	}
}