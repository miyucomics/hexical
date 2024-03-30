package miyucomics.hexical.registry

import miyucomics.hexical.advancements.EducateGenieCriterion
import miyucomics.hexical.advancements.ReloadLampCriterion
import miyucomics.hexical.advancements.UseUpLampCriterion
import net.minecraft.advancement.criterion.Criteria

object HexicalAdvancements {
	val EDUCATE_GENIE: EducateGenieCriterion = Criteria.register(EducateGenieCriterion())
	val RELOAD_LAMP: ReloadLampCriterion = Criteria.register(ReloadLampCriterion())
	val USE_UP_LAMP: UseUpLampCriterion = Criteria.register(UseUpLampCriterion())

	@JvmStatic fun init() {}
}