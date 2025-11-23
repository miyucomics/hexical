package miyucomics.hexical.features.item_cache

import at.petrak.hexcasting.api.utils.getCompound
import at.petrak.hexcasting.api.utils.getList
import at.petrak.hexcasting.api.utils.getString
import miyucomics.hexical.features.player.types.PlayerTicker
import miyucomics.hexical.features.wristpocket.wristpocket
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.misc.HexSerialization
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtElement
import net.minecraft.server.network.ServerPlayerEntity

// Hexical adds a lot of items that require quick and easy access, e.g. driver dots, scarabs, and grimoires
// Rather than do an inventory scan *per-pattern* which would outrageously tank performance,
// there is an item cache that is mixined into every player and updates at the start of every tick
// Then, it is trivial to look up whether a driver dot hex is registered for a given pattern
// or if a given pattern has a grimoire expansion associated with it
// or if the player has a scarab
class PlayerItemCacheTicker : PlayerTicker {
	override fun tick(player: PlayerEntity) {
		if (player !is ServerPlayerEntity)
			return

		player.itemCache().driverDotsMacros.clear()
		player.itemCache().grimoireMacros.clear()
		val inventory = player.inventory
		inventory.offHand.plus(inventory.main).plus(inventory.armor).plus(player.wristpocket).plus(player.enderChestInventory.stacks).reversed().forEach { stack ->
			when {
				stack.isOf(HexicalItems.GRIMOIRE_ITEM) -> {
					val expansions = stack.getCompound("expansions") ?: return@forEach
					expansions.keys.forEach { player.itemCache().grimoireMacros[it] = HexSerialization.backwardsCompatibleReadHex(expansions, it, player.serverWorld) }
				}
				stack.isOf(HexicalItems.DRIVER_DOT_ITEM) && stack.hasNbt() && stack.nbt!!.contains("pattern") -> {
					player.itemCache().driverDotsMacros[stack.getString("pattern") ?: return@forEach] = HexSerialization.deserializeHex(stack.getList("program", NbtElement.COMPOUND_TYPE.toInt()) ?: return@forEach, player.serverWorld)
				}
				stack.isOf(HexicalItems.SCARAB_BEETLE_ITEM) && stack.hasNbt() && stack.nbt!!.getBoolean("active") -> {
					val program = stack.getList("hex", NbtElement.COMPOUND_TYPE.toInt()) ?: return@forEach
					player.itemCache().scarabProgram = HexSerialization.deserializeHex(program, player.serverWorld)
				}
			}
		}
	}
}