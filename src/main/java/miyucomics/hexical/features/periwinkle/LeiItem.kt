package miyucomics.hexical.features.periwinkle

import at.petrak.hexcasting.common.lib.HexAttributes
import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import java.util.*

object LeiItem : ArmorItem(LeiArmorMaterial.INSTANCE, Type.HELMET, Settings()) {
	private var bakedAttributes: Multimap<EntityAttribute, EntityAttributeModifier>

	init {
		val attributes = ImmutableMultimap.builder<EntityAttribute, EntityAttributeModifier>()
		attributes.put(HexAttributes.GRID_ZOOM, EntityAttributeModifier(UUID.fromString("9794eabc-2eec-42ee-b10a-c7d1fcd3de74"), "Scrying Lens Zoom", 0.25, EntityAttributeModifier.Operation.MULTIPLY_TOTAL))
		bakedAttributes = attributes.build()
	}

	override fun useOnEntity(stack: ItemStack, player: PlayerEntity, friend: LivingEntity, hand: Hand): ActionResult {
		if (friend is PlayerEntity && friend.getEquippedStack(EquipmentSlot.HEAD).isEmpty) {
			friend.equipStack(EquipmentSlot.HEAD, stack.copy())
			stack.decrement(1)
			return ActionResult.SUCCESS
		}
		return ActionResult.PASS
	}

	override fun getAttributeModifiers(equipmentSlot: EquipmentSlot): Multimap<EntityAttribute, EntityAttributeModifier> {
		if (equipmentSlot == EquipmentSlot.HEAD)
			return bakedAttributes
		return super.getAttributeModifiers(equipmentSlot)
	}
}