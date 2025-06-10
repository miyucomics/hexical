package miyucomics.hexical.casting.patterns.hotbar

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.getPositiveIntUnderInclusive
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket
import net.minecraft.server.network.ServerPlayerEntity

class OpSetHotbar : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env !is PlayerBasedCastEnv)
			throw MishapBadCaster()
		val slot = args.getPositiveIntUnderInclusive(0, 8, argc)
		(env.castingEntity as ServerPlayerEntity).networkHandler.sendPacket(UpdateSelectedSlotS2CPacket(slot))
		return listOf()
	}
}