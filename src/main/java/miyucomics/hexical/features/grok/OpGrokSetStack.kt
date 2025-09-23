package miyucomics.hexical.features.grok

import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.casting.mishaps.MishapOthersName
import net.minecraft.server.network.ServerPlayerEntity

object OpGrokSetStack : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env.castingEntity !is ServerPlayerEntity)
			throw MishapBadCaster()
		val newStack = args.getList(0, argc).map { if (MishapOthersName.getTrueNameFromDatum(it, env.castingEntity as ServerPlayerEntity) == null) it else NullIota() }
		return SpellAction.Result(OpGrokSetParenthesized.Spell { image -> image.copy(stack = newStack.toList()) }, 0, listOf())
	}
}