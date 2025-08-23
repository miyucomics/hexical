package miyucomics.hexical.features.grok

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
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

		var newIota: Iota
		val newImage = if (image.parenCount == 0) {
			val stack = image.stack.toMutableList()
			newIota = if (stack.isEmpty())
				GarbageIota()
			else
				stack.removeAt(stack.lastIndex)
			image.copy(stack = stack)
		} else {
			val parenthesized = image.parenthesized.toMutableList()
			newIota = if (parenthesized.isEmpty())
				GarbageIota()
			else
				parenthesized.removeAt(parenthesized.lastIndex).iota
			image.copy(parenthesized = parenthesized)
		}
		IXplatAbstractions.INSTANCE.setStaffcastImage(player, newImage)

		return listOf(newIota)
	}
}