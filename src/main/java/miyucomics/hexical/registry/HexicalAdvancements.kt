package miyucomics.hexical.registry

import miyucomics.hexical.advancements.*
import net.minecraft.advancement.criterion.Criteria

object HexicalAdvancements {
	val AR: ARCriterion = Criteria.register(ARCriterion())
	val DIY: DIYCriterion = Criteria.register(DIYCriterion())
	val HALLUCINATE: HallucinateCriterion = Criteria.register(HallucinateCriterion())
	val EDUCATE_GENIE: EducateGenieCriterion = Criteria.register(EducateGenieCriterion())
	val RELOAD_LAMP: ReloadLampCriterion = Criteria.register(ReloadLampCriterion())
	val SCRY: ScryCriterion = Criteria.register(ScryCriterion())
	val USE_UP_LAMP: UseUpLampCriterion = Criteria.register(UseUpLampCriterion())

	@JvmStatic
	fun init() {}
}