package miyucomics.hexical.items

import at.petrak.hexcasting.common.items.ItemStaff
import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents

class LightningRodStaff : ItemStaff(Settings().maxCount(1).group(HexicalItems.HEXICAL_GROUP)) {
	private val attributeModifiers: Multimap<EntityAttribute, EntityAttributeModifier>

	init {
		val builder = ImmutableMultimap.builder<EntityAttribute, EntityAttributeModifier>()
		builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", -3.2, EntityAttributeModifier.Operation.ADDITION))
		this.attributeModifiers = builder.build()
	}

	override fun getAttributeModifiers(slot: EquipmentSlot?): Multimap<EntityAttribute, EntityAttributeModifier> {
		if (slot == EquipmentSlot.MAINHAND)
			return this.attributeModifiers
		return super.getAttributeModifiers(slot)
	}

	override fun postHit(stack: ItemStack?, target: LivingEntity?, attacker: LivingEntity?): Boolean {
		target!!.world.playSound(target.x, target.y, target.z, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.MASTER, 3f, 1f, true)
		val velocity = target.pos.subtract(attacker!!.pos).normalize().multiply(1.5)
		target.addVelocity(velocity.x, velocity.y, velocity.z)
		return super.postHit(stack, target, attacker)
	}
}