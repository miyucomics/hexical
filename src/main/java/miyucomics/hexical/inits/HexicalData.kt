package miyucomics.hexical.inits

import miyucomics.hexical.features.dyes.DyeData
import miyucomics.hexical.features.hopper.HopperEndpointRegistry
import miyucomics.hexical.features.prestidigitation.PrestidigitationData

object HexicalData {
	fun init() {
		DyeData.init()
		HopperEndpointRegistry.init()
		PrestidigitationData.init()
	}
}