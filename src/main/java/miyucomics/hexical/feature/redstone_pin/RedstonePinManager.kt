package miyucomics.hexical.feature.redstone_pin

import at.petrak.hexcasting.api.utils.putList
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.block.Blocks
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtOps
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.PersistentState
import kotlin.jvm.optionals.getOrNull

object RedstonePinManager : InitHook() {
	fun pin(world: ServerWorld, position: BlockPos, power: Int, ticks: Int) = getPersistentState(world).pin(world, position, power, ticks)
	private fun getPersistentState(world: ServerWorld): DimensionalPins = world.persistentStateManager.getOrCreate(DimensionalPins::createFromNbt, ::DimensionalPins, "pins")
	@JvmStatic fun hasMagicalPower(world: ServerWorld, position: BlockPos) = getPersistentState(world).isPinned(position)
	@JvmStatic fun getMagicalPower(world: ServerWorld, position: BlockPos) = getPersistentState(world).getPinnedStrength(position)

	override fun init() {
		ServerTickEvents.END_WORLD_TICK.register { getPersistentState(it).tick(it) }
	}
}

private class DimensionalPins(private val instances: HashMap<BlockPos, PinInstance> = HashMap()) : PersistentState() {
	constructor() : this(HashMap())

	fun isPinned(position: BlockPos) = instances.containsKey(position)
	fun getPinnedStrength(position: BlockPos) = instances[position]!!.power

	fun pin(world: ServerWorld, position: BlockPos, power: Int, ticks: Int) {
		instances[position] = PinInstance(position, power, ticks)
		triggerBlockUpdate(world, position)
	}

	fun tick(world: ServerWorld) {
		instances.replaceAll { _, pin -> pin.copy(time = pin.time - 1) }
		val toRemove = instances.filter { (_, pin) -> pin.time <= 0 }.map { (position, _) -> position }
		toRemove.forEach(instances::remove)
		toRemove.forEach { triggerBlockUpdate(world, it) }
	}

	override fun writeNbt(nbt: NbtCompound): NbtCompound {
		val list = NbtList()
		instances.mapNotNull { (_, instance) -> PinInstance.CODEC.encodeStart(NbtOps.INSTANCE, instance).result().getOrNull() }.forEach(list::add)
		return NbtCompound().apply { putList("pins", list) }
	}

	companion object {
		fun createFromNbt(nbt: NbtCompound): DimensionalPins {
			val pins = nbt.getList("pins", NbtElement.COMPOUND_TYPE.toInt())
				.mapNotNull { PinInstance.CODEC.parse(NbtOps.INSTANCE, it).result().getOrNull() }
				.associateBy { it.position }
			return DimensionalPins(HashMap(pins))
		}

		private fun triggerBlockUpdate(world: ServerWorld, pos: BlockPos) {
			world.getBlockState(pos).neighborUpdate(world, pos, Blocks.AIR, pos, false)
		}
	}
}

data class PinInstance(val position: BlockPos, val power: Int, val time: Int) {
	companion object {
		val CODEC: Codec<PinInstance> = RecordCodecBuilder.create { it.group(
			BlockPos.CODEC.fieldOf("position").forGetter(PinInstance::position),
			Codec.INT.fieldOf("power").forGetter(PinInstance::power),
			Codec.INT.fieldOf("time").forGetter(PinInstance::time)
		).apply(it, ::PinInstance) }
	}
}