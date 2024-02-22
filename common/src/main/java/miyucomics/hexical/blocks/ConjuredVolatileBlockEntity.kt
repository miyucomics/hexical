package miyucomics.hexical.blocks

import miyucomics.hexical.generics.GenericConjuredBlockEntity
import miyucomics.hexical.registry.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos

class ConjuredVolatileBlockEntity(type: BlockEntityType<ConjuredVolatileBlockEntity>, pos: BlockPos?, state: BlockState?) : GenericConjuredBlockEntity<ConjuredVolatileBlockEntity>(pos, state, type) {
	companion object {
		fun init (pos: BlockPos?, state: BlockState?): ConjuredVolatileBlockEntity {
			return ConjuredVolatileBlockEntity(HexicalBlocks.CONJURED_VOLATILE_BLOCK_ENTITY, pos, state)
		}
	}
}