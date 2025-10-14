package miyucomics.hexical.inits

import com.google.gson.JsonObject
import miyucomics.hexical.HexicalMain
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.advancement.criterion.AbstractCriterionConditions
import net.minecraft.advancement.criterion.Criteria
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer
import net.minecraft.predicate.entity.LootContextPredicate
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

object HexicalAdvancements {
	val AR: SpecklikeCriterion = Criteria.register(SpecklikeCriterion())
	val CONJURE_CAKE: ConjureCakeCriterion = Criteria.register(ConjureCakeCriterion())
	val HEXXY: HexxyCriterion = Criteria.register(HexxyCriterion())
	val DIY: DIYCriterion = Criteria.register(DIYCriterion())
	val EDUCATE_GENIE: EducateGenieCriterion = Criteria.register(EducateGenieCriterion())
	val RELOAD_LAMP: ReloadLampCriterion = Criteria.register(ReloadLampCriterion())

	val EVOCATION_STATISTIC: Identifier = HexicalMain.id("evocation")

	fun init() {
		Registry.register(Registries.CUSTOM_STAT, EVOCATION_STATISTIC, EVOCATION_STATISTIC)
	}
}

abstract class BaseCriterion<T : BaseCriterion.BaseCondition>(private val id: Identifier) : AbstractCriterion<T>() {
	override fun conditionsFromJson(obj: JsonObject, playerPredicate: LootContextPredicate, predicateDeserializer: AdvancementEntityPredicateDeserializer): T = createCondition()
	abstract class BaseCondition(id: Identifier) : AbstractCriterionConditions(id, LootContextPredicate.EMPTY)
	fun trigger(player: ServerPlayerEntity) = trigger(player) { true }
	protected abstract fun createCondition(): T
	override fun getId(): Identifier = id
}

class ConjureCakeCriterion : BaseCriterion<ConjureCakeCriterion.Condition>(HexicalMain.id("conjure_cake")) {
	override fun createCondition() = Condition()
	class Condition : BaseCondition(HexicalMain.id("conjure_cake"))
}

class DIYCriterion : BaseCriterion<DIYCriterion.Condition>(HexicalMain.id("diy_conjuring")) {
	override fun createCondition() = Condition()
	class Condition : BaseCondition(HexicalMain.id("diy_conjuring"))
}

class HexxyCriterion : BaseCriterion<HexxyCriterion.Condition>(HexicalMain.id("summon_hexxy")) {
	override fun createCondition() = Condition()
	class Condition : BaseCondition(HexicalMain.id("summon_hexxy"))
}

class EducateGenieCriterion : BaseCriterion<EducateGenieCriterion.Condition>(HexicalMain.id("educate_genie")) {
	override fun createCondition() = Condition()
	class Condition : BaseCondition(HexicalMain.id("educate_genie"))
}

class ReloadLampCriterion : BaseCriterion<ReloadLampCriterion.Condition>(HexicalMain.id("reload_lamp")) {
	override fun createCondition() = Condition()
	class Condition : BaseCondition(HexicalMain.id("reload_lamp"))
}

class SpecklikeCriterion : BaseCriterion<SpecklikeCriterion.Condition>(HexicalMain.id("specklike")) {
	override fun createCondition() = Condition()
	class Condition : BaseCondition(HexicalMain.id("specklike"))
}