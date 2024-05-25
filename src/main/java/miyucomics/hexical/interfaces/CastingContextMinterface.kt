package miyucomics.hexical.interfaces

import miyucomics.hexical.enums.SpecializedSource

interface CastingContextMinterface {
	fun setSpecializedSource(source: SpecializedSource)
	fun getSpecializedSource(): SpecializedSource?
	fun setFinale(finale: Boolean)
	fun getFinale(): Boolean
}