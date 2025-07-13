package miyucomics.hexical.features.items

import miyucomics.hexical.misc.ClientStorage
import miyucomics.hexical.features.player.fields.syncMediaLog
import net.minecraft.entity.LivingEntity
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
		if (!world.isClient)
			(player as ServerPlayerEntity).syncMediaLog()
		else {
			ClientStorage.fadingInLog = true
			ClientStorage.fadingInLogStart = ClientStorage.ticks
		}
		player.setCurrentHand(hand)
		return TypedActionResult.success(stack)
	}

	override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity, remainingUseTicks: Int) {
		if (world.isClient)
			ClientStorage.fadingInLog = false
	}

	override fun getTranslationKey() = "item.hexical.media_log." + ((System.currentTimeMillis() / 2000) % 2).toString()
}