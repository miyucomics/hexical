package miyucomics.hexical.features.mage_blocks

import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtElement
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

interface MageBlockModifier {
	val type: MageBlockModifierType<*>
	fun serialize(): NbtElement
	fun tick(world: ServerWorld, pos: BlockPos, state: BlockState) {}
}