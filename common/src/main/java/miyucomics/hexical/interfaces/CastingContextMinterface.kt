package miyucomics.hexical.interfaces

interface CastingContextMinterface {
	fun getCastByLamp(): Boolean
	fun getArchLamp(): Boolean
	fun getFinale(): Boolean
	fun setCastByLamp(castByLamp: Boolean)
	fun setArchLamp(archLamp: Boolean)
	fun setFinale(finale: Boolean)
}