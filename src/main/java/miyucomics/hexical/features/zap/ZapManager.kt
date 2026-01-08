package miyucomics.hexical.features.zap

import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.block.Blocks
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

object ZapManager : InitHook() {
	private val instances: MutableMap<ServerWorld, HashMap<BlockPos, ZapInstance>> = HashMap()

	fun triggerRedstone(world: ServerWorld, position: BlockPos, power: Int, ticks: Int) {
		val specific = instances.getOrPut(world, ::HashMap)
		specific[position] = ZapInstance(world, position, power, ticks)
		triggerBlockUpdate(world, position)
	}

	@JvmStatic fun hasMagicalPower(world: ServerWorld, position: BlockPos) = instances.getOrPut(world, ::HashMap).containsKey(position)
	@JvmStatic fun getMagicalPower(world: ServerWorld, position: BlockPos) = instances.getOrPut(world, ::HashMap)[position]!!.power

	override fun init() {
		ServerTickEvents.END_WORLD_TICK.register(::tick)
	}

	private fun tick(world: ServerWorld) {
		val specific = instances.getOrPut(world, ::HashMap)
		specific.replaceAll { _, instance -> instance.copy(time = instance.time - 1) }
		val toRemove = specific.filter { (_, instance) -> instance.time <= 0 }.map { (position, _) -> position }
		toRemove.forEach(specific::remove)
		toRemove.forEach { triggerBlockUpdate(world, it) }
	}

	private fun triggerBlockUpdate(world: ServerWorld, pos: BlockPos) {
		world.getBlockState(pos).neighborUpdate(world, pos, Blocks.AIR, pos, false)
	}
}