package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

class UseItemOnEffect(val stack: ItemStack) : PrestidigitationEffect {
	override fun getCost() = MediaConstants.DUST_UNIT
	override fun effectBlock(caster: ServerPlayerEntity, position: BlockPos) {
		val oldStack = caster.mainHandStack
		caster.setStackInHand(Hand.MAIN_HAND, stack)
		caster.world.getBlockState(position).onUse(caster.world, caster, Hand.MAIN_HAND, BlockHitResult(Vec3d.of(position), Direction.DOWN, position, false))
		caster.setStackInHand(Hand.MAIN_HAND, oldStack)
	}
	override fun effectEntity(caster: ServerPlayerEntity, entity: Entity) {
		val oldStack = caster.mainHandStack
		caster.setStackInHand(Hand.MAIN_HAND, stack)
		entity.interact(caster, Hand.MAIN_HAND)
		caster.setStackInHand(Hand.MAIN_HAND, oldStack)
	}
}