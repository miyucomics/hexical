package miyucomics.hexical.features.evocation

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import miyucomics.hexical.misc.SerializationUtils
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity

class OpGetEvocation : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env.castingEntity !is ServerPlayerEntity)
			throw MishapBadCaster()
		val deserialized = SerializationUtils.deserializeHex((env.castingEntity as PlayerEntity).evocation, env.world)
		return listOf(ListIota(deserialized))
	}
}