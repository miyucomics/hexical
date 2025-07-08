package miyucomics.hexical.registry

import miyucomics.hexical.data.DyeData
import miyucomics.hexical.data.hopper.HopperEndpointRegistry
import miyucomics.hexical.data.prestidigitation.PrestidigitationData

object HexicalData {
	fun init() {
		DyeData.init()
		HopperEndpointRegistry.init()
		PrestidigitationData.init()
	}
}