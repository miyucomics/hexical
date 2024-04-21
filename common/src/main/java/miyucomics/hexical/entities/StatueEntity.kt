package miyucomics.hexical.entities

import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
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
		return ItemStack(Items.CRYING_OBSIDIAN)
	}

	override fun getMainArm(): Arm {
		return Arm.RIGHT
	}

	companion object {
		fun createMobAttribute() {

		}
	}
}