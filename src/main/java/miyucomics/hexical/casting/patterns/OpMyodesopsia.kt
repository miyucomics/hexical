package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.msgs.MsgNewSpiralPatternsS2C
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.server.network.ServerPlayerEntity

class OpMyodesopsia : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val list = args.getList(0, argc)
		return SpellAction.Result(Spell(list.toList()), 0L, listOf())
	}

	private data class Spell(val list: List<Iota>) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val caster = env.castingEntity
			if (caster !is ServerPlayerEntity)
				return
			val patterns: List<HexPattern> = list.stream().filter { i: Iota? -> i is PatternIota }.map { i: Iota -> (i as PatternIota).pattern }.toList()
			val packet = MsgNewSpiralPatternsS2C(caster.uuid, patterns, 140)
			IXplatAbstractions.INSTANCE.sendPacketToPlayer(caster, packet)
			IXplatAbstractions.INSTANCE.sendPacketTracking(caster, packet)
		}
	}
}