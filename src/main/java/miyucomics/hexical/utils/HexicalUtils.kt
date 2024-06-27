package miyucomics.hexical.utils

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.enums.SpecializedSource
import miyucomics.hexical.interfaces.CastingContextMinterface
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld

@Suppress("CAST_NEVER_SUCCEEDS")
object HexicalUtils {
	@JvmStatic
	fun isEnlightened(player: ServerPlayerEntity): Boolean {
		val advancement = player.getServer()!!.advancementLoader[HexAPI.modLoc("enlightenment")]
		val tracker = player.advancementTracker
		if (tracker.getProgress(advancement) != null)
			return tracker.getProgress(advancement).isDone
		return false
	}

	@JvmStatic
	fun castSpecial(world: ServerWorld, user: ServerPlayerEntity, hex: List<Iota>, source: SpecializedSource, finale: Boolean) {
		val ctx = CastingContext(user, user.activeHand, CastingContext.CastSource.PACKAGED_HEX)
		(ctx as CastingContextMinterface).setSpecializedSource(source)
		(ctx as CastingContextMinterface).setFinale(finale)
		CastingHarness(ctx).executeIotas(hex, world)
	}
}