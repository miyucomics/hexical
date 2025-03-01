package miyucomics.hexical.casting.patterns.evocation

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import miyucomics.hexical.interfaces.PlayerEntityMinterface
import net.minecraft.server.network.ServerPlayerEntity

class OpGetEvocation : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env.castingEntity !is ServerPlayerEntity)
			throw MishapBadCaster()
		val deserialized = IotaType.deserialize((env.castingEntity as PlayerEntityMinterface).getEvocation(), env.world)
		if (deserialized is GarbageIota)
			return listOf(NullIota())
		return listOf(deserialized)
	}
}