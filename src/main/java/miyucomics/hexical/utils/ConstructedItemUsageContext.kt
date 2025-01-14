package miyucomics.hexical.utils

import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class ConstructedItemUsageContext(world: World, position: BlockPos, normal: Direction, private val horizontalNormal: Direction, stack: ItemStack, hand: Hand) : ItemUsageContext(world, null, hand, stack, BlockHitResult(Vec3d.ofCenter(position.add(normal.vector)), normal, position, false)) {
	override fun getHorizontalPlayerFacing() = horizontalNormal
	override fun shouldCancelInteraction() = false
}