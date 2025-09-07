package miyucomics.hexical.features.grok

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.casting.mishaps.MishapOthersName
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand

object OpGrokPull : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env !is PlayerBasedCastEnv)
			throw MishapBadCaster()
		val player = env.castingEntity as ServerPlayerEntity
		val image = IXplatAbstractions.INSTANCE.getStaffcastVM(player, Hand.MAIN_HAND).image
		val stack = image.stack.map { if (MishapOthersName.getTrueNameFromDatum(it, env.castingEntity as? ServerPlayerEntity) == null) it else NullIota() }
		val parentheized = image.parenthesized.map { if (MishapOthersName.getTrueNameFromDatum(it.iota, env.castingEntity as? ServerPlayerEntity) == null) it.iota else NullIota() }
		return listOf(ListIota(stack), ListIota(parentheized))
	}
}