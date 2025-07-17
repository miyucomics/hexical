package miyucomics.hexical.features.lesser_sentinels

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d

class OpLesserSentinelSet : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env.castingEntity !is ServerPlayerEntity)
			throw MishapBadCaster()
		val positions = args.getList(0, argc).map {
			if (it !is Vec3Iota)
				throw MishapInvalidIota.of(args[0], 0, "lesser_sentinel_list")
			it.vec3
		}
		return SpellAction.Result(Spell(positions), 0, listOf())
	}

	private data class Spell(val pos: List<Vec3d>) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val caster = (env.castingEntity as ServerPlayerEntity)
			caster.currentLesserSentinels = pos.toMutableList()
			caster.syncLesserSentinels()
		}
	}
}