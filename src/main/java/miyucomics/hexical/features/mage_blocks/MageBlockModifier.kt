package miyucomics.hexical.features.mage_blocks

import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtElement
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

interface MageBlockModifier {
	val id: String
	val argc: Int
		get() = 0

	fun getArguments(args: List<Iota>): List<Any> = listOf()
	fun modify(args: List<Any>) {}
	fun tick(world: ServerWorld, pos: BlockPos, state: BlockState) {}

	fun writeNbt(): NbtElement
	fun readNbt(element: NbtElement)
}