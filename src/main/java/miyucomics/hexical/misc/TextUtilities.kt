package miyucomics.hexical.misc

import at.petrak.hexcasting.api.pigment.FrozenPigment
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import java.math.RoundingMode
import java.text.DecimalFormat

object TextUtilities {
	val PERCENTAGE: DecimalFormat = DecimalFormat("####")
	val DUST_AMOUNT: DecimalFormat = DecimalFormat("###,###.##")

	init {
		PERCENTAGE.roundingMode = RoundingMode.DOWN
	}

	fun getPigmentedText(string: String, pigment: FrozenPigment, offset: Float = 0f): MutableText {
		return string.foldIndexed(Text.empty()) { index, acc, char -> acc.append(Text.literal(char.toString()).styled { it.withColor(pigment.colorProvider.getColor(offset, Vec3d(0.0, index * 0.5, 0.0)) and 0x00FFFFFF) }) }
	}
}