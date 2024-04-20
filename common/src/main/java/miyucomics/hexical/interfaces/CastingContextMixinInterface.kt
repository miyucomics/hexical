package miyucomics.hexical.interfaces

interface CastingContextMixinInterface {
	fun `hexical$getCastByLamp`(): Boolean
	fun `hexical$getArchLamp`(): Boolean
	fun `hexical$getFinale`(): Boolean
	fun `hexical$setCastByLamp`(castByLamp: Boolean)
	fun `hexical$setArchLamp`(archLamp: Boolean)
	fun `hexical$setFinale`(finale: Boolean)
}