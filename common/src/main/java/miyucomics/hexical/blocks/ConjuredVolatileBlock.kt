package miyucomics.hexical.blocks

import miyucomics.hexical.generics.GenericConjuredBlock
import miyucomics.hexical.registry.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

class ConjuredVolatileBlock : GenericConjuredBlock<ConjuredVolatileBlockEntity>(baseMaterial(), ConjuredVolatileBlockEntity::init) {
	private val offsets: List<Vec3i> = listOf(
		Vec3i(-1, 0, 0), Vec3i(1, 0, 0),
		Vec3i(0, -1, 0), Vec3i(0, 1, 0),
		Vec3i(0, 0, -1), Vec3i(0, 0, 1),
	)

	override fun onBreak(world: World, position: BlockPos, state: BlockState, player: PlayerEntity) {
		world.setBlockState(position, Blocks.AIR.defaultState)
		world.playSound(position.x.toDouble(), position.y.toDouble(), position.z.toDouble(), SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.BLOCKS, 1f, 1f, true)
		for (offset in offsets) {
			val positionToTest = position.add(offset);
			val otherState = world.getBlockState(positionToTest)
			val block = otherState.block
			if (block == HexicalBlocks.CONJURED_VOLATILE_BLOCK)
				block.onBreak(world, positionToTest, otherState, player)
		}
	}
}