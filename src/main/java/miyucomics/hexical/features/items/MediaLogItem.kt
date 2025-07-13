package miyucomics.hexical.features.items

import miyucomics.hexical.features.player_state.fields.syncLedger
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class MediaLogItem : Item(Settings().maxCount(1)) {
	override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
		val stack = player.getStackInHand(hand)
		(player as ServerPlayerEntity).syncLedger()
		return TypedActionResult.success(stack)
	}

	override fun getTranslationKey() = "item.hexical.media_log." + ((System.currentTimeMillis() / 2000) % 2).toString()
}