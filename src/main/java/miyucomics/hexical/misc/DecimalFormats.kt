package miyucomics.hexical.misc

import java.math.RoundingMode
import java.text.DecimalFormat

object DecimalFormats {
	val PERCENTAGE: DecimalFormat = DecimalFormat("####")
	val DUST_AMOUNT: DecimalFormat = DecimalFormat("###,###.##")

	init {
		PERCENTAGE.roundingMode = RoundingMode.DOWN
	}
}