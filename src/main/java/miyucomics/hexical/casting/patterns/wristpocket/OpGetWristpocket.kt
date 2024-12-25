package miyucomics.hexical.casting.patterns.wristpocket

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexical.state.PersistentStateHandler
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

class OpGetWristpocket(private val process: (ItemStack) -> List<Iota>) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val caster = env.castingEntity
		if (caster !is ServerPlayerEntity)
			return listOf(NullIota())
		return process(PersistentStateHandler.getWristpocketStack(caster))
	}
}