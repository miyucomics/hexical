package miyucomics.hexical.blocks

import miyucomics.hexical.generics.GenericConjuredBlockEntity
import miyucomics.hexical.registry.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos

class ConjuredSlipperyBlockEntity(type: BlockEntityType<ConjuredSlipperyBlockEntity>, pos: BlockPos?, state: BlockState?) : GenericConjuredBlockEntity<ConjuredSlipperyBlockEntity>(pos, state, type) {
	companion object {
		fun init (pos: BlockPos?, state: BlockState?): ConjuredSlipperyBlockEntity {
			return ConjuredSlipperyBlockEntity(HexicalBlocks.CONJURED_SLIPPERY_BLOCK_ENTITY, pos, state)
		}
	}
}