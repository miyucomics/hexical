package miyucomics.hexical.features.mage_blocks

import miyucomics.hexical.features.mage_blocks.modifiers.BouncyModifier
import miyucomics.hexical.features.mage_blocks.modifiers.RedstoneModifier
import miyucomics.hexical.features.mage_blocks.modifiers.VolatileModifier
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class MageBlock : Block(Settings.create().nonOpaque().dropsNothing().breakInstantly().mapColor(MapColor.CLEAR).suffocates { _, _, _ -> false }.blockVision { _, _, _ -> false }.allowsSpawning { _, _, _, _ -> false }.sounds(BlockSoundGroup.AMETHYST_CLUSTER)), BlockEntityProvider {
	override fun emitsRedstonePower(state: BlockState) = true
	override fun getWeakRedstonePower(state: BlockState, world: BlockView, pos: BlockPos, direction: Direction): Int {
		val blockEntity = world.getBlockEntity(pos)
		if (blockEntity !is MageBlockEntity)
			return 0
		if (blockEntity.hasModifier(RedstoneModifier.TYPE))
			return blockEntity.getModifier(RedstoneModifier.TYPE).power
		return 0
	}


	override fun onLandedUpon(world: World, state: BlockState, pos: BlockPos, entity: Entity, fallDistance: Float) {
		val blockEntity = world.getBlockEntity(pos) as MageBlockEntity
		if (!blockEntity.hasModifier(BouncyModifier.TYPE))
			super.onLandedUpon(world, state, pos, entity, fallDistance)
	}

	override fun onEntityLand(world: BlockView, entity: Entity) {
		val blockEntity = world.getBlockEntity(entity.blockPos.add(0, -1, 0))
		if (blockEntity !is MageBlockEntity)
			return
		if (blockEntity.hasModifier(BouncyModifier.TYPE)) {
			val velocity = entity.velocity
			if (velocity.y < 0) {
				entity.setVelocity(velocity.x, -velocity.y, velocity.z)
				entity.fallDistance = 0f
			}
		} else
			super.onEntityLand(world, entity)
	}

	override fun onBreak(world: World, position: BlockPos, state: BlockState, player: PlayerEntity?) {
		val blockEntity = world.getBlockEntity(position) as MageBlockEntity
		world.setBlockState(position, Blocks.AIR.defaultState)

		if (blockEntity.hasModifier(VolatileModifier.TYPE)) {
			for (offset in Direction.stream()) {
				val positionToTest = position.add(offset.vector)
				val otherState = world.getBlockState(positionToTest)
				val block = otherState.block
				if (block == HexicalBlocks.MAGE_BLOCK)
					block.onBreak(world, positionToTest, otherState, player)
			}
		}

		super.onBreak(world, position, state, player)
	}

	override fun createBlockEntity(pos: BlockPos, state: BlockState) = MageBlockEntity(pos, state)
	override fun <T : BlockEntity> getTicker(pworld: World, pstate: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T> = BlockEntityTicker { world, position, state, blockEntity ->
		(blockEntity as MageBlockEntity).modifiers.forEach { it -> it.value.tick(world, position, state) }
	}

	// defer shapes to disguise
	private fun getBlockDisguise(world: BlockView, pos: BlockPos): BlockState? = (world.getBlockEntity(pos) as? MageBlockEntity)?.disguise

	override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape =
		getBlockDisguise(world, pos)?.getOutlineShape(world, pos, context) ?: VoxelShapes.fullCube()

	override fun getCullingShape(state: BlockState, world: BlockView, pos: BlockPos): VoxelShape =
		getBlockDisguise(world, pos)?.getCullingShape(world, pos) ?: VoxelShapes.fullCube()

	override fun getRaycastShape(state: BlockState, world: BlockView, pos: BlockPos): VoxelShape =
		getBlockDisguise(world, pos)?.getRaycastShape(world, pos) ?: VoxelShapes.fullCube()

	override fun getCollisionShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape =
		getBlockDisguise(world, pos)?.getCollisionShape(world, pos, context) ?: VoxelShapes.fullCube()

	override fun getCameraCollisionShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape =
		getBlockDisguise(world, pos)?.getCameraCollisionShape(world, pos, context) ?: VoxelShapes.fullCube()
}