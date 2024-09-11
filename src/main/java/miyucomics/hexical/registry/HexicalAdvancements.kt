package miyucomics.hexical.registry

import miyucomics.hexical.advancements.*
import net.minecraft.advancement.criterion.Criteria

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