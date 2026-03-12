package miyucomics.hexical.features.dyes.entity

import miyucomics.hexical.features.dyes.DyeOption
import net.minecraft.entity.Entity

abstract class DyeEntityHandler<T>(private val handledClass: Class<T>) {
	fun canAffectEntity(entity: Entity, dye: DyeOption): Boolean = handledClass.isInstance(entity) && validate(entity as T, dye)
	open fun validate(entity: T, dye: DyeOption): Boolean = true
	abstract fun affect(entity: T, dye: DyeOption)

	fun affectSafely(entity: Entity, dye: DyeOption) {
		if (canAffectEntity(entity, dye)) {
			@Suppress("UNCHECKED_CAST")
			affect(entity as T, dye)
		} else {
			throw IllegalStateException("Spent media but unable to affect entity?")
		}
	}
}