package miyucomics.hexical.utils

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapOthersName
import miyucomics.hexical.inits.HexicalItems
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

object CastingUtils {
	fun assertNoTruename(iota: Iota, env: CastingEnvironment) {
		val original = if (env.castingEntity is ServerPlayerEntity) env.castingEntity as ServerPlayerEntity else null
		val truename = MishapOthersName.getTrueNameFromDatum(iota, original)
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
}