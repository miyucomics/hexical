package miyucomics.hexical.registry

import miyucomics.hexical.data.DyeData
import miyucomics.hexical.data.prestidigitation.PrestidigitationData

object HexicalData {
	fun init() {
		DyeData.init()
		PrestidigitationData.init()
	}
}