package miyucomics.hexical.casting.actions.evocation

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.features.evocation.evocation
import miyucomics.hexical.misc.CastingUtils
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity

class OpSetEvocation : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env.castingEntity !is ServerPlayerEntity)
			throw MishapBadCaster()
		args.getList(0, argc)
		CastingUtils.assertNoTruename(args[0], env)
		return SpellAction.Result(Spell(args[0]), MediaConstants.CRYSTAL_UNIT, listOf())
	}

	private data class Spell(val hex: Iota) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			(env.castingEntity as PlayerEntity).evocation = IotaType.serialize(hex)
		}
	}
}