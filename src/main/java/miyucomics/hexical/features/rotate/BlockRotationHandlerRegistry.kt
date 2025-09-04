package miyucomics.hexical.features.rotate

import net.minecraft.block.BlockState
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction

object BlockRotationHandlerRegistry {
    private val handlers = mutableListOf<BlockRotationHandler>()

    fun init() {
        register(object : BlockRotationHandler {
            override fun canHandle(state: BlockState, direction: Direction) = state.properties.contains(Properties.FACING)
            override fun handle(state: BlockState, direction: Direction) = state.with(Properties.FACING, direction)
        })

        register(object : BlockRotationHandler {
            override fun canHandle(state: BlockState, direction: Direction) = state.properties.contains(Properties.HOPPER_FACING) && direction != Direction.UP
            override fun handle(state: BlockState, direction: Direction) = state.with(Properties.HOPPER_FACING, direction)
        })

        register(object : BlockRotationHandler {
            override fun canHandle(state: BlockState, direction: Direction) = state.properties.contains(Properties.HORIZONTAL_FACING) && direction != Direction.UP
            override fun handle(state: BlockState, direction: Direction) = state.with(Properties.HORIZONTAL_FACING, direction)
        })
    }

    fun register(handler: BlockRotationHandler) {
        handlers += handler
    }
}