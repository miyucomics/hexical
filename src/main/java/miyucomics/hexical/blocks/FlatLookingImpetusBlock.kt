package miyucomics.hexical.blocks

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class FlatLookingImpetusBlock : FlatImpetusBlock() {
	override fun createBlockEntity(pos: BlockPos, state: BlockState) = FlatLookingImpetusBlockEntity(pos, state)

	override fun <T : BlockEntity> getTicker(world: World, blockState: BlockState, blockEntityType: BlockEntityType<T>): BlockEntityTicker<T>? {
		if (world.isClient)
			return null
		return BlockEntityTicker { world1, pos, state1, blockEntity ->
			FlatLookingImpetusBlockEntity.serverTick(
				world1,
				pos,
				state1,
				blockEntity as FlatLookingImpetusBlockEntity
			)
		}
	}
}