package miyucomics.hexical.items

import miyucomics.hexical.entities.SpikeEntity
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult

class SpikeItem : Item(Settings().group(HexicalItems.HEXICAL_GROUP)) {
	override fun useOnBlock(context: ItemUsageContext): ActionResult {
		val direction = context.side
		val position = context.blockPos.offset(direction)
		context.world.spawnEntity(SpikeEntity(context.world, position.x.toDouble(), position.y.toDouble(), position.z.toDouble(), direction, 10))
		return ActionResult.SUCCESS
	}
}