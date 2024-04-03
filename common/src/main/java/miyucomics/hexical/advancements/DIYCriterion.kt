package miyucomics.hexical.advancements

import com.google.gson.JsonObject
import miyucomics.hexical.Hexical
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.advancement.criterion.AbstractCriterionConditions
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

class DIYCriterion : AbstractCriterion<DIYCriterion.Condition>() {
	override fun conditionsFromJson(obj: JsonObject, playerPredicate: EntityPredicate.Extended, predicateDeserializer: AdvancementEntityPredicateDeserializer) = Condition()
	fun trigger(player: ServerPlayerEntity?) = trigger(player) { true }
	override fun getId() = ID

	class Condition : AbstractCriterionConditions(ID, EntityPredicate.Extended.EMPTY)
	companion object {
		val ID: Identifier = Identifier(Hexical.MOD_ID, "diy_conjuring")
	}
}