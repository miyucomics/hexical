package miyucomics.hexical.items

import miyucomics.hexical.data.LedgerData
import miyucomics.hexical.registry.HexicalNetworking
import miyucomics.hexical.screens.LedgerScreen
import miyucomics.hexical.state.PersistentStateHandler
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class LedgerItem : Item(Settings().maxCount(1)) {
	override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
		if (world.isClient) {
			MinecraftClient.getInstance().setScreen(LedgerScreen())
		} else
			ServerPlayNetworking.send(player as ServerPlayerEntity, HexicalNetworking.LEDGER_CHANNEL, LedgerData.getLedger(player).toPacket())
		return TypedActionResult.success(player.getStackInHand(hand))
	}
}