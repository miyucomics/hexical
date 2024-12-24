package miyucomics.hexical.inits

import com.google.gson.JsonObject
import miyucomics.hexical.HexicalMain
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.advancement.criterion.AbstractCriterionConditions
import net.minecraft.advancement.criterion.Criteria
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer
import net.minecraft.predicate.entity.LootContextPredicate
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

object HexicalAdvancements {
	lateinit var AR: ARCriterion
	lateinit var HEXXY: HexxyCriterion
	lateinit var DIY: DIYCriterion
	lateinit var HALLUCINATE: HallucinateCriterion
	lateinit var EDUCATE_GENIE: EducateGenieCriterion
	lateinit var RELOAD_LAMP: ReloadLampCriterion

	@JvmStatic
	fun init() {
		AR = Criteria.register(ARCriterion())
		HEXXY = Criteria.register(HexxyCriterion())
		DIY = Criteria.register(DIYCriterion())
		HALLUCINATE = Criteria.register(HallucinateCriterion())
		EDUCATE_GENIE = Criteria.register(EducateGenieCriterion())
		RELOAD_LAMP = Criteria.register(ReloadLampCriterion())
	}
}

abstract class BaseCriterion<T : BaseCriterion.BaseCondition>(private val id: Identifier) : AbstractCriterion<T>() {
	override fun conditionsFromJson(obj: JsonObject, playerPredicate: LootContextPredicate, predicateDeserializer: AdvancementEntityPredicateDeserializer): T = createCondition()
	abstract class BaseCondition(id: Identifier) : AbstractCriterionConditions(id, LootContextPredicate.EMPTY)
	fun trigger(player: ServerPlayerEntity) = trigger(player) { true }
	protected abstract fun createCondition(): T
	override fun getId(): Identifier = id
}

class ARCriterion : BaseCriterion<ARCriterion.Condition>(HexicalMain.id("augmented_reality")) {
	override fun createCondition() = Condition()
	class Condition : BaseCondition(HexicalMain.id("augmented_reality"))
}

class DIYCriterion : BaseCriterion<DIYCriterion.Condition>(HexicalMain.id("diy_conjuring")) {
	override fun createCondition() = Condition()
	class Condition : BaseCondition(HexicalMain.id("diy_conjuring"))
}

class HexxyCriterion : BaseCriterion<HexxyCriterion.Condition>(HexicalMain.id("summon_hexxy")) {
	override fun createCondition() = Condition()
	class Condition : BaseCondition(HexicalMain.id("summon_hexxy"))
}

class HallucinateCriterion : BaseCriterion<HallucinateCriterion.Condition>(HexicalMain.id("hallucinate")) {
	override fun createCondition() = Condition()
	class Condition : BaseCondition(HexicalMain.id("hallucinate"))
}

class EducateGenieCriterion : BaseCriterion<EducateGenieCriterion.Condition>(HexicalMain.id("educate_genie")) {
	override fun createCondition() = Condition()
	class Condition : BaseCondition(HexicalMain.id("educate_genie"))
}

class ReloadLampCriterion : BaseCriterion<ReloadLampCriterion.Condition>(HexicalMain.id("reload_lamp")) {
	override fun createCondition() = Condition()
	class Condition : BaseCondition(HexicalMain.id("reload_lamp"))
}