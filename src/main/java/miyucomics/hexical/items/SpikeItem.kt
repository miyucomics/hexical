package miyucomics.hexical.items

import miyucomics.hexical.entities.SpikeEntity
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult
import net.minecraft.util.math.Vec3d

class SpikeItem : Item(Settings().group(HexicalItems.HEXICAL_GROUP)) {
	override fun useOnBlock(context: ItemUsageContext): ActionResult {
		val direction = context.side
		val position = Vec3d.ofBottomCenter(context.blockPos).add(Vec3d.of(direction.vector))
		context.world.spawnEntity(SpikeEntity(context.world, position.x, position.y, position.z, direction, 10))
		return ActionResult.SUCCESS
	}
}