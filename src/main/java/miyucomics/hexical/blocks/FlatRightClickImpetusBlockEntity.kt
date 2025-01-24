package miyucomics.hexical.blocks

import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.enums.WallMountLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

class FlatRightClickImpetusBlockEntity(position: BlockPos, state: BlockState) : BlockEntityAbstractImpetus(HexicalBlocks.FLAT_RIGHT_CLICK_IMPETUS_BLOCK_ENTITY, position, state) {
	override fun getStartDirection(): Direction {
		if (this.cachedState.get(FlatImpetusBlock.ATTACH_FACE) == WallMountLocation.WALL)
			return Direction.DOWN
		return super.getStartDirection()
	}
}