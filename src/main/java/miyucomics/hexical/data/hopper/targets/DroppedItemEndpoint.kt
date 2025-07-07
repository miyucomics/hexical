package miyucomics.hexical.data.hopper.targets

import miyucomics.hexical.data.hopper.HopperEndpoint
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d

class DroppedItemEndpoint(private val pos: Vec3d, private val world: ServerWorld) : HopperEndpoint {
	override fun insert(stack: ItemStack): ItemStack {
		if (stack.isEmpty)
			return ItemStack.EMPTY
		val dropped = stack.copy()
		val entity = ItemEntity(world, pos.x, pos.y, pos.z, dropped)
		entity.setToDefaultPickupDelay()
		world.spawnEntity(entity)
		return ItemStack.EMPTY
	}
}