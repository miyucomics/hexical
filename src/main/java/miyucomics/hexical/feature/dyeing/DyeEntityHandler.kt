package miyucomics.hexical.feature.dyeing

import net.minecraft.entity.Entity

class DyeEntityHandler<T : Entity>(private val type: Class<T>, private val color: (T) -> DyeOption?, private val canDye: (T, DyeOption) -> Boolean, private val dye: (T, DyeOption) -> Unit) {
	fun appliesTo(entity: Entity, dye: DyeOption) = type.isInstance(entity) && canDye(type.cast(entity), dye)
	fun colorOf(entity: Entity): DyeOption? = if (type.isInstance(entity)) color(type.cast(entity)) else null
	fun applyTo(entity: Entity, dye: DyeOption) {
		check(appliesTo(entity, dye)) { "Spent media but unable to affect entity?" }
		this.dye(type.cast(entity), dye)
	}
}

inline fun <reified T : Entity> dyeHandler(noinline color: (T) -> DyeOption? = { null }, noinline canDye: (T, DyeOption) -> Boolean = { _, _ -> true }, noinline dye: (T, DyeOption) -> Unit) = DyeEntityHandler(T::class.java, color, canDye, dye)