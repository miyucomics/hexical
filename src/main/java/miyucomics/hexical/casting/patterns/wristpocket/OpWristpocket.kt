package miyucomics.hexical.casting.patterns.wristpocket

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.state.PersistentStateHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand

class OpWristpocket : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val caster = env.castingEntity
		if (caster !is ServerPlayerEntity)
			throw MishapBadCaster()
		return SpellAction.Result(Spell(env.otherHand), MediaConstants.DUST_UNIT / 8, listOf())
	}

	private data class Spell(val hand: Hand) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val caster = env.castingEntity
			if (caster !is ServerPlayerEntity)
				throw MishapBadCaster()
			val item = PersistentStateHandler.getWristpocketStack(caster)
			PersistentStateHandler.setWristpocketStack(caster, caster.getStackInHand(hand))
			caster.setStackInHand(hand, item)
		}
	}
}