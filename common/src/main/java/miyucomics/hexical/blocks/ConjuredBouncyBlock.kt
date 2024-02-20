package miyucomics.hexical.blocks

import at.petrak.hexcasting.annotations.SoftImplement
import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.common.blocks.BlockConjured
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

class ConjuredBouncyBlock(properties: Settings?) : BlockConjured(properties) {
	override fun createBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity? {
		return ConjuredBouncyBlockEntity(pPos, pState)
	}

	override fun onEntityLand(world: BlockView, entity: Entity) {
		val velocity = entity.velocity
		if (velocity.y < 0)
			entity.setVelocity(velocity.x, -velocity.y, velocity.z)
	}

	override fun <T : BlockEntity?> getTicker(pLevel: World, pState: BlockState?, pBlockEntityType: BlockEntityType<T>?): BlockEntityTicker<T>? {
		return if (pLevel.isClient) BlockEntityTicker { level: World?, blockPos: BlockPos?, blockState: BlockState?, t: T -> tick(level, blockPos, blockState, t) } else null
	}

	override fun onSteppedOn(pLevel: World, pPos: BlockPos, pState: BlockState, pEntity: Entity) {
		val tile = pLevel.getBlockEntity(pPos)
		if (tile is ConjuredBouncyBlockEntity)
			tile.walkParticle(pEntity)
	}

	companion object {
		fun <T> tick(level: World?, blockPos: BlockPos?, blockState: BlockState?, t: T) {
			if (t is ConjuredBouncyBlockEntity)
				t.particleEffect()
		}

		fun setColor(pLevel: WorldAccess, pPos: BlockPos, colorizer: FrozenColorizer) {
			val blockentity = pLevel.getBlockEntity(pPos)
			if (blockentity is ConjuredBouncyBlockEntity)
				blockentity.setColorizer(colorizer)
		}
	}
}