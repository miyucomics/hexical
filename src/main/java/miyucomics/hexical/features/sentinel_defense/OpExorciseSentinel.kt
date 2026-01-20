package miyucomics.hexical.features.sentinel_defense

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.server.network.ServerPlayerEntity

object OpExorciseSentinel : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val center = args.getVec3(0, argc)
		env.assertVecInRange(center)
		val data = env.world.players.associateWith(IXplatAbstractions.INSTANCE::getSentinel)
			.filter { (_, sentinel) -> sentinel != null && sentinel.extendsRange && sentinel.dimension == env.world.registryKey && sentinel.position().squaredDistanceTo(center) <= 0.01 }
			.minByOrNull { (_, sentinel) -> sentinel!!.position.squaredDistanceTo(center) }
		return SpellAction.Result(Spell(data?.key), MediaConstants.CRYSTAL_UNIT * 3, listOf(ParticleSpray.burst(center, 3.0)))
	}

	private data class Spell(val player: ServerPlayerEntity?) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			if (player != null)
				IXplatAbstractions.INSTANCE.setSentinel(player, null)
		}
	}
}