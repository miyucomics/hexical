package miyucomics.hexical.interfaces

import at.petrak.hexcasting.api.misc.FrozenColorizer

interface Specklike {
	fun setRoll(rotation: Float)
	fun setSize(size: Float)
	fun setThickness(thickness: Float)
	fun setLifespan(lifespan: Int)
	fun setPigment(pigment: FrozenColorizer)

	fun getRoll(): Float
	fun getSize(): Float
	fun getThickness(): Float
	fun getPigment(): FrozenColorizer
}