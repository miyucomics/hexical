package miyucomics.hexical.features.periwinkle.enchantments

import at.petrak.hexcasting.common.lib.HexAttributes
import com.google.common.collect.Multimap
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import java.util.*

object AnalyzingEnchantment : AbstractLeiEnchantment() {
	val SCRY_SIGHT_BOOST: UUID = UUID.fromString("71563fe4-1ad3-406c-ba24-5d9b70223514")
	override fun modifyAttributes(original: Multimap<EntityAttribute, EntityAttributeModifier>) {
		original.put(HexAttributes.SCRY_SIGHT, EntityAttributeModifier(SCRY_SIGHT_BOOST, "Analyzing Boost", 1.0, EntityAttributeModifier.Operation.ADDITION))
	}
}