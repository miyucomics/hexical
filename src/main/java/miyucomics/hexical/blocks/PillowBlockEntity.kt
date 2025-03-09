package miyucomics.hexical.blocks

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.registry.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.math.abs

class PillowBlockEntity(pos: BlockPos, state: BlockState?) : BlockEntity(HexicalBlocks.PILLOW_BLOCK_ENTITY, pos, state) {
	fun tick(world: World, pos: BlockPos) {

	}
}