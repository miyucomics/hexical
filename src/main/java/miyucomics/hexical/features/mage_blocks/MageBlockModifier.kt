package miyucomics.hexical.features.mage_blocks

import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtElement
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

interface MageBlockModifier {
	val type: MageBlockModifierType<*>
	fun serialize(): NbtElement
	fun tick(world: World, pos: BlockPos, state: BlockState) {}
}