package miyucomics.hexical.features.sentinel_defense

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.xplat.IXplatAbstractions

object OpHuntSentinel : ConstMediaAction {
	override val argc = 1
	override val mediaCost = MediaConstants.DUST_UNIT / 100
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val center = args.getVec3(0, argc)
		env.assertVecInRange(center)
		val sentinel = env.world.players.mapNotNull(IXplatAbstractions.INSTANCE::getSentinel)
			.filter { it.extendsRange && it.dimension == env.world.registryKey && it.position().squaredDistanceTo(center) <= 256 }
			.minByOrNull { it.position.squaredDistanceTo(center) }
			?: return listOf(NullIota())
		return sentinel.position.subtract(center).normalize().asActionResult
	}
}