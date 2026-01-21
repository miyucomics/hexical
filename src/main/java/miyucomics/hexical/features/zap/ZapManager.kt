package miyucomics.hexical.features.zap

import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

object ZapManager : InitHook() {
	fun getPersistentState(world: ServerWorld): ZapState = world.persistentStateManager.getOrCreate(ZapState::createFromNbt, ::ZapState, "zaps")
	fun zap(world: ServerWorld, position: BlockPos, power: Int, ticks: Int) = getPersistentState(world).zap(world, position, power, ticks)
	@JvmStatic fun hasMagicalPower(world: ServerWorld, position: BlockPos) = getPersistentState(world).hasMagicalPower(position)
	@JvmStatic fun getMagicalPower(world: ServerWorld, position: BlockPos) = getPersistentState(world).getMagicalPower(position)

	override fun init() {
		ServerTickEvents.END_WORLD_TICK.register { getPersistentState(it).tick(it) }
	}
}