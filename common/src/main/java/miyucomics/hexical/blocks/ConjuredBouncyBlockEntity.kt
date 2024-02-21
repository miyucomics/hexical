package miyucomics.hexical.blocks

import miyucomics.hexical.generics.GenericConjuredBlockEntity
import miyucomics.hexical.registry.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos

class ConjuredBouncyBlockEntity(type: BlockEntityType<ConjuredBouncyBlockEntity>, pos: BlockPos?, state: BlockState?) : GenericConjuredBlockEntity<ConjuredBouncyBlockEntity>(pos, state, type) {
	companion object {
		fun init (pos: BlockPos?, state: BlockState?): ConjuredBouncyBlockEntity {
			return ConjuredBouncyBlockEntity(HexicalBlocks.CONJURED_BOUNCY_BLOCK_ENTITY, pos, state)
		}
	}
}