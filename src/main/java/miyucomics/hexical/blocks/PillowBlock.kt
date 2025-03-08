package miyucomics.hexical.blocks

import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class PillowBlock : Block(Settings.copy(Blocks.DEEPSLATE_TILES).strength(4f, 4f)), BlockEntityProvider {
	override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = PillowBlockEntity(pos, state)
	override fun <T : BlockEntity> getTicker(world: World, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T> = BlockEntityTicker { world1, pos, _, blockEntity -> (blockEntity as PillowBlockEntity).tick(world1, pos) }
}