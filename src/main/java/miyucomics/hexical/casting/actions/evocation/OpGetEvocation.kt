package miyucomics.hexical.casting.actions.evocation

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import miyucomics.hexical.features.evocation.evocation
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity

class OpGetEvocation : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env.castingEntity !is ServerPlayerEntity)
			throw MishapBadCaster()
		val deserialized = IotaType.deserialize((env.castingEntity as PlayerEntity).evocation, env.world)
		if (deserialized is GarbageIota)
			return listOf(NullIota())
		return listOf(deserialized)
	}
}