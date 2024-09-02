package miyucomics.hexical.interfaces

import miyucomics.hexical.enums.InjectedGambit

interface FrameForEachMinterface {
	fun overwrite(gambit: InjectedGambit)
	fun getInjectedGambit(): InjectedGambit
}