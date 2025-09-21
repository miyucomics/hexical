package miyucomics.hexical.features.media_log

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World

object MediaLogItem : Item(Settings().maxCount(1)) {
	override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
		val stack = player.getStackInHand(hand)
		if (!world.isClient)
			(player as ServerPlayerEntity).syncMediaLog()
		player.setCurrentHand(hand)
		return TypedActionResult.success(stack)
	}

	override fun getUseAction(itemStack: ItemStack) = UseAction.BOW
	override fun getTranslationKey() = "item.hexical.media_log." + ((System.currentTimeMillis() / 2000) % 2).toString()
}