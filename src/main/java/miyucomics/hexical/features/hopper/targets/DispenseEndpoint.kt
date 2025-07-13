package miyucomics.hexical.features.hopper.targets

import miyucomics.hexical.features.hopper.HopperDestination
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d

class DispenseEndpoint(private val pos: Vec3d, private val world: ServerWorld) : HopperDestination {
	override fun simulateDeposit(stack: ItemStack) = stack.count

	override fun deposit(stack: ItemStack): ItemStack {
		if (stack.isEmpty)
			return ItemStack.EMPTY
		val dropped = stack.copy()
		val entity = ItemEntity(world, pos.x, pos.y, pos.z, dropped)
		entity.setToDefaultPickupDelay()
		world.spawnEntity(entity)
		return ItemStack.EMPTY
	}
}