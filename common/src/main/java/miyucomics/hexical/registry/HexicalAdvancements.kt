package miyucomics.hexical.registry

import com.google.gson.JsonObject
import miyucomics.hexical.Hexical
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.advancement.criterion.AbstractCriterionConditions
import net.minecraft.advancement.criterion.Criteria
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

object HexicalAdvancements {
	val AR: CriterionTemplate = Criteria.register(CriterionTemplate(Hexical.id("augmented_reality")))
	val DIY: CriterionTemplate = Criteria.register(CriterionTemplate(Hexical.id("diy_conjuring")))
	val HALLUCINATE: CriterionTemplate = Criteria.register(CriterionTemplate(Hexical.id("hallucinate")))
	val EDUCATE_GENIE: CriterionTemplate = Criteria.register(CriterionTemplate(Hexical.id("educate_genie")))
	val RELOAD_LAMP: CriterionTemplate = Criteria.register(CriterionTemplate(Hexical.id("reload_lamp")))
	val USE_UP_LAMP: CriterionTemplate = Criteria.register(CriterionTemplate(Hexical.id("use_up_lamp")))

	@JvmStatic
	fun init() {
	}
}

class CriterionTemplate(val data: Identifier) : AbstractCriterion<CriterionTemplate.Condition>() {
	override fun conditionsFromJson(obj: JsonObject, playerPredicate: EntityPredicate.Extended, predicateDeserializer: AdvancementEntityPredicateDeserializer) = Condition()
	inner class Condition : AbstractCriterionConditions(data, EntityPredicate.Extended.EMPTY)
	fun trigger(player: ServerPlayerEntity?) = trigger(player) { true }
	override fun getId() = data
}