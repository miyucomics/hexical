package miyucomics.hexical.features.rotate

import miyucomics.hexical.misc.InitHook
import net.minecraft.block.BlockState
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction

object BlockRotationHandlerRegistry : InitHook() {
    private val handlers = mutableListOf<BlockRotationHandler>()

    fun modify(state: BlockState, direction: Direction) = handlers.firstOrNull { it.canHandle(state, direction) }?.handle(state, direction)

    override fun init() {
        register(object : BlockRotationHandler {
            override fun canHandle(state: BlockState, direction: Direction) = state.properties.contains(Properties.AXIS)
            override fun handle(state: BlockState, direction: Direction) = state.with(Properties.AXIS, direction.axis)
        })

        register(object : BlockRotationHandler {
            override fun canHandle(state: BlockState, direction: Direction) = state.properties.contains(Properties.FACING)
            override fun handle(state: BlockState, direction: Direction) = state.with(Properties.FACING, direction)
        })

        register(object : BlockRotationHandler {
            override fun canHandle(state: BlockState, direction: Direction) = state.properties.contains(Properties.HOPPER_FACING) && direction != Direction.UP
            override fun handle(state: BlockState, direction: Direction) = state.with(Properties.HOPPER_FACING, direction)
        })

        register(object : BlockRotationHandler {
            override fun canHandle(state: BlockState, direction: Direction) = state.properties.contains(Properties.HORIZONTAL_FACING) && direction != Direction.UP  && direction != Direction.DOWN
            override fun handle(state: BlockState, direction: Direction) = state.with(Properties.HORIZONTAL_FACING, direction)
        })

        register(object : BlockRotationHandler {
            override fun canHandle(state: BlockState, direction: Direction) = state.properties.contains(Properties.VERTICAL_DIRECTION) && !(direction == Direction.UP || direction == Direction.DOWN)
            override fun handle(state: BlockState, direction: Direction) = state.with(Properties.VERTICAL_DIRECTION, direction)
        })
    }

    fun register(handler: BlockRotationHandler) {
        handlers += handler
    }
}