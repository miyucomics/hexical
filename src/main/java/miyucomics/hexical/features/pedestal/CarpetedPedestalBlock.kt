package miyucomics.hexical.features.pedestal

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class CarpetedPedestalBlock : PedestalBlock() {
	override fun createBlockEntity(pos: BlockPos, state: BlockState) = CarpetedPedestalBlockEntity(pos, state)
}