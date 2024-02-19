package miyucomics.hexical.blocks

import at.petrak.hexcasting.common.blocks.BlockConjured
import at.petrak.hexcasting.common.blocks.entity.BlockEntityConjured
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

class ConjuredBouncyBlock(properties: Settings?) : BlockConjured(properties) {
	override fun onLandedUpon(world: World, state: BlockState, pos: BlockPos, entity: Entity, fallDistance: Float) {
		val blockentity = world.getBlockEntity(pos)
		if (blockentity is BlockEntityConjured)
			blockentity.landParticle(entity, 10)
	}

	override fun onEntityLand(world: BlockView, entity: Entity) {
		val velocity = entity.velocity
		if (velocity.y < 0)
			entity.setVelocity(velocity.x, -velocity.y, velocity.z)
	}
}