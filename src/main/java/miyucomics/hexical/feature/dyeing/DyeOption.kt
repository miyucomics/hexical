package miyucomics.hexical.feature.dyeing

import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.DyeColor

enum class DyeOption(val color: Int, val dye: DyeColor?) {
	UNCOLORED(0xFF00FF, null),
	WHITE(0xF9FFFE, DyeColor.WHITE),
	ORANGE(16351261, DyeColor.ORANGE),
	MAGENTA(13061821, DyeColor.MAGENTA),
	LIGHT_BLUE(3847130, DyeColor.LIGHT_BLUE),
	YELLOW(16701501, DyeColor.YELLOW),
	LIME(8439583, DyeColor.LIME),
	PINK(15961002, DyeColor.PINK),
	GRAY(4673362, DyeColor.GRAY),
	LIGHT_GRAY(0x9D9D97, DyeColor.LIGHT_GRAY),
	CYAN(1481884, DyeColor.CYAN),
	PURPLE(8991416, DyeColor.PURPLE),
	BLUE(3949738, DyeColor.BLUE),
	BROWN(8606770, DyeColor.BROWN),
	GREEN(6192150, DyeColor.GREEN),
	RED(11546150, DyeColor.RED),
	BLACK(0x1D1D21, DyeColor.BLACK);

	val key = name.lowercase() // "light_blue", "uncolored". Used as the JSON key and the lang key suffix
	val slug = if (dye == null) "" else key // "light_blue" or "" for uncolored. For patterns like `dye_colorizer_{color}`
	val prefix = if (dye == null) "" else "${key}_"  // "light_blue_" or "" for uncolored. For patterns like `{color_}wool`
	val text: Text = Text.translatable("dye.hexical.$key").setStyle(Style.EMPTY.withColor(color))

	companion object {
		private val byDye = entries.associateBy { it.dye }
		private val byKey = entries.associateBy { it.key }
		fun fromDyeColor(dye: DyeColor?) = byDye.getValue(dye)
		fun fromKey(key: String) = byKey[key]
	}
}