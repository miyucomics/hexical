package miyucomics.hexical.misc

import miyucomics.hexical.features.wristpocket.wristpocket
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

object HexItemsFinder {
	// used for grimoires and scarabs
	fun getMatchingItem(player: ServerPlayerEntity, predicate: (ItemStack) -> Boolean): ItemStack? {
		val inventory = player.inventory
		return inventory.main
			.plus(inventory.armor)
			.plus(inventory.offHand)
			.plus(player.enderChestInventory.stacks)
			.plus(player.wristpocket)
			.firstOrNull(predicate)
	}
}