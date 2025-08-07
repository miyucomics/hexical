package miyucomics.hexical.features.mage_blocks

import com.mojang.datafixers.util.Pair
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtElement
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

interface MageBlockModifier {
	val type: MageBlockModifierType<*>
	fun serialize(): NbtElement
	fun getScryingLens(): Pair<ItemStack, Text>? = null
	fun tick(world: World, pos: BlockPos, state: BlockState) {}
}