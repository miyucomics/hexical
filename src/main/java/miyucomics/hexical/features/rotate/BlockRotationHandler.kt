package miyucomics.hexical.features.rotate

import net.minecraft.block.BlockState
import net.minecraft.util.math.Direction

interface BlockRotationHandler {
    fun canHandle(state: BlockState, direction: Direction): Boolean
    fun handle(state: BlockState, direction: Direction): BlockState
}