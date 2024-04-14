package miyucomics.hexical.items

import miyucomics.hexical.entities.LivingScrollEntity
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.event.GameEvent

class LivingScrollItem(private val size: Int) : Item(Settings().group(HexicalItems.HEXICAL_GROUP)) {
	override fun useOnBlock(ctx: ItemUsageContext?): ActionResult {
		val direction = ctx!!.side
		val clickPosition = ctx.blockPos
		val position = clickPosition.offset(direction)

		val stack = ctx.stack
		val player = ctx.player ?: return ActionResult.FAIL
		if (direction.axis.isVertical || !player.canPlaceOn(position, direction, stack))
			return ActionResult.FAIL

		val world = ctx.world
		val scrollEntity = LivingScrollEntity(world, position, direction, size)
		if (!scrollEntity.canStayAttached())
			return ActionResult.CONSUME

		if (!world.isClient) {
			scrollEntity.onPlace()
			world.emitGameEvent(player, GameEvent.ENTITY_PLACE, clickPosition)
			world.spawnEntity(scrollEntity)
		}
		stack.decrement(1)
		return ActionResult.success(world.isClient)
	}
}