package miyucomics.hexical.misc

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage.ParenthesizedIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapOthersName
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand

object CastingUtils {
	fun assertNoTruename(iota: Iota, env: CastingEnvironment) {
		val truename = MishapOthersName.getTrueNameFromDatum(iota, env.castingEntity as? ServerPlayerEntity)
		if (truename != null)
			throw MishapOthersName(truename)
	}

	@JvmStatic
	fun isEnlightened(player: ServerPlayerEntity): Boolean {
		val advancement = player.getServer()!!.advancementLoader[HexAPI.modLoc("enlightenment")]
		val tracker = player.advancementTracker
		if (tracker.getProgress(advancement) != null)
			return tracker.getProgress(advancement).isDone
		return false
	}

	@JvmStatic
	fun giveIota(player: ServerPlayerEntity, iota: Iota) {
		val image = IXplatAbstractions.INSTANCE.getStaffcastVM(player, Hand.MAIN_HAND).image
		val newImage = if (image.parenCount == 0) {
			val stack = image.stack.toMutableList()
			stack.add(iota)
			image.copy(stack = stack)
		} else {
			val parenthesized = image.parenthesized.toMutableList()
			parenthesized.add(ParenthesizedIota(iota, false))
			image.copy(parenthesized = parenthesized)
		}
		IXplatAbstractions.INSTANCE.setStaffcastImage(player, newImage)
	}
}