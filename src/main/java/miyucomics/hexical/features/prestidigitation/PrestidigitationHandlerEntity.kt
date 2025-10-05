package miyucomics.hexical.features.prestidigitation

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import net.minecraft.entity.Entity

abstract class PrestidigitationHandlerEntity<T>(private val handledClass: Class<T>) : PrestidigitationHandler {
	open fun canAffectEntity(env: CastingEnvironment, entity: Entity): Boolean = handledClass.isInstance(entity)
	abstract fun affect(env: CastingEnvironment, entity: T)

	fun affectSafely(env: CastingEnvironment, entity: Entity) {
		if (canAffectEntity(env, entity)) {
			@Suppress("UNCHECKED_CAST")
			affect(env, entity as T)
		} else {
			throw IllegalStateException("Spent media but unable to affect entity?")
		}
	}
}