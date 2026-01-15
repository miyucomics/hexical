package miyucomics.hexical.features.zap

import at.petrak.hexcasting.api.utils.putList
import net.minecraft.block.Blocks
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtOps
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.PersistentState
import kotlin.jvm.optionals.getOrNull

class ZapState(private val instances: HashMap<BlockPos, ZapInstance> = HashMap()) : PersistentState() {
	constructor() : this(HashMap())

	fun hasMagicalPower(position: BlockPos) = instances.containsKey(position)
	fun getMagicalPower(position: BlockPos) = instances[position]!!.power

	fun zap(world: ServerWorld, position: BlockPos, power: Int, ticks: Int) {
		instances[position] = ZapInstance(position, power, ticks)
		triggerBlockUpdate(world, position)
	}

	fun tick(world: ServerWorld) {
		instances.replaceAll { _, zap -> zap.copy(time = zap.time - 1) }
		val toRemove = instances.filter { (_, zap) -> zap.time <= 0 }.map { (position, _) -> position }
		toRemove.forEach(instances::remove)
		toRemove.forEach { triggerBlockUpdate(world, it) }
	}

	override fun writeNbt(nbt: NbtCompound): NbtCompound {
		val list = NbtList()
		instances.mapNotNull { (_, instance) -> ZapInstance.CODEC.encodeStart(NbtOps.INSTANCE, instance).result().getOrNull() }.forEach(list::add)
		return NbtCompound().apply { putList("zaps", list) }
	}

	companion object {
		fun createFromNbt(nbt: NbtCompound): ZapState {
			val zaps = nbt.getList("zaps", NbtElement.COMPOUND_TYPE.toInt())
				.mapNotNull { ZapInstance.CODEC.parse(NbtOps.INSTANCE, it).result().getOrNull() }
				.associateBy { it.position }
			return ZapState(HashMap(zaps))
		}

		private fun triggerBlockUpdate(world: ServerWorld, pos: BlockPos) {
			world.getBlockState(pos).neighborUpdate(world, pos, Blocks.AIR, pos, false)
		}
	}
}