package miyucomics.hexical.utils

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.misc.DiscoveryHandlers
import at.petrak.hexcasting.api.misc.HexDamageSources
import at.petrak.hexcasting.api.mod.HexConfig
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.Mishap
import at.petrak.hexcasting.api.spell.mishaps.MishapOthersName
import at.petrak.hexcasting.api.utils.compareMediaItem
import at.petrak.hexcasting.api.utils.extractMedia
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.enums.SpecializedSource
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.interfaces.CastingContextMinterface
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import kotlin.math.ceil
import kotlin.math.max

@Suppress("CAST_NEVER_SUCCEEDS")
object CastingUtils {
	@JvmStatic
	fun takeMediaFromInventory(harness: CastingHarness, cost: Int): Int {
		var remainingCost = cost

		for (source in DiscoveryHandlers.collectMediaHolders(harness).sortedWith(Comparator(::compareMediaItem).reversed())) {
			remainingCost -= extractMedia(source, remainingCost, simulate = false)
			if (remainingCost <= 0)
				break
		}

		if (remainingCost > 0) {
			val mediaToHealth = HexConfig.common().mediaToHealthRate()
			val requiredBloodMedia = max(remainingCost.toDouble() / mediaToHealth, 0.5)
			val availableBloodMedia = harness.ctx.caster.health * mediaToHealth
			Mishap.trulyHurt(harness.ctx.caster, HexDamageSources.OVERCAST, requiredBloodMedia.toFloat())
			remainingCost -= ceil(availableBloodMedia - (harness.ctx.caster.health * mediaToHealth)).toInt()
		}

		return remainingCost
	}

	@JvmStatic
	fun assertNoTruename(iota: Iota, caster: ServerPlayerEntity) {
		val trueName = MishapOthersName.getTrueNameFromDatum(iota, caster)
		if (trueName != null)
			throw MishapOthersName(trueName)
	}

	@JvmStatic
	fun getActiveArchLamp(player: ServerPlayerEntity): ItemStack? {
		val combinedInventory = listOf(player.inventory.main, player.inventory.armor, player.inventory.offHand)
		for (inventory in combinedInventory)
			for (stack in inventory)
				if (stack.isOf(HexicalItems.ARCH_LAMP_ITEM) && stack.getOrCreateNbt().getBoolean("active"))
					return stack
		return null
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
	fun castSpecial(world: ServerWorld, user: ServerPlayerEntity, hex: List<Iota>, source: SpecializedSource, finale: Boolean): CastingHarness {
		val hand = if (!user.getStackInHand(Hand.MAIN_HAND).isEmpty && user.getStackInHand(Hand.OFF_HAND).isEmpty) Hand.OFF_HAND else Hand.MAIN_HAND
		val harness = CastingHarness(CastingContext(user, hand, CastingContext.CastSource.PACKAGED_HEX))
		(harness.ctx as CastingContextMinterface).setSpecializedSource(source)
		(harness.ctx as CastingContextMinterface).setFinale(finale)
		harness.executeIotas(hex, world)
		return harness
	}
}