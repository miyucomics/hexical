package miyucomics.hexical.features.dyes

import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.DyeColor

enum class DyeOption(val replacement: String, val color: Int, val dyeColor: DyeColor?) {
	UNCOLORED("", 0xFF00FF, null),
	WHITE("white", 0xF9FFFE, DyeColor.WHITE),
	ORANGE("orange", 16351261, DyeColor.ORANGE),
	MAGENTA("magenta", 13061821, DyeColor.MAGENTA),
	LIGHT_BLUE("light_blue", 3847130, DyeColor.LIGHT_BLUE),
	YELLOW("yellow", 16701501, DyeColor.YELLOW),
	LIME("lime", 8439583, DyeColor.LIME),
	PINK("pink", 15961002, DyeColor.PINK),
	GRAY("gray", 4673362, DyeColor.GRAY),
	LIGHT_GRAY("light_gray", 0x9D9D97, DyeColor.LIGHT_GRAY),
	CYAN("cyan", 1481884, DyeColor.CYAN),
	PURPLE("purple", 8991416, DyeColor.PURPLE),
	BLUE("blue", 3949738, DyeColor.BLUE),
	BROWN("brown", 8606770, DyeColor.BROWN),
	GREEN("green", 6192150, DyeColor.GREEN),
	RED("red", 11546150, DyeColor.RED),
	BLACK("black", 0x1D1D21, DyeColor.BLACK);

	val coloredText: Text get() = Text.translatable("dye.hexical.${name.lowercase()}").setStyle(Style.EMPTY.withColor(color))
}