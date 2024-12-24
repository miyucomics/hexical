package miyucomics.hexical.utils

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapOthersName
import miyucomics.hexical.inits.HexicalItems
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

object CastingUtils {
	fun assertNoTruename(iota: Iota, caster: LivingEntity?) {
		if (caster !is PlayerEntity)
			return
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
}