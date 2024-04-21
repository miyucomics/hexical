package miyucomics.hexical.entities

import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.MobEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Arm
import net.minecraft.world.World

class StatueEntity(entityType: EntityType<out LivingEntity?>?, world: World?) : LivingEntity(entityType, world) {
	override fun getArmorItems(): MutableIterable<ItemStack> {
		return mutableListOf()
	}

	override fun equipStack(slot: EquipmentSlot?, stack: ItemStack?) {
	}

	override fun getEquippedStack(slot: EquipmentSlot?): ItemStack {
		if (slot == EquipmentSlot.MAINHAND)
			return ItemStack(Items.CRYING_OBSIDIAN)
		return ItemStack.EMPTY
	}

	override fun getMainArm(): Arm {
		return Arm.RIGHT
	}

	companion object {
		fun createMobAttribute(): DefaultAttributeContainer.Builder {
			return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0)
		}
	}
}