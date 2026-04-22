package miyucomics.hexical.feature.dyeing

import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.DyeColor

enum class DyeOption(val replacement: String, val color: Int, val dye: DyeColor?, val text: Text) {
	UNCOLORED("", 0xFF00FF, null,  Text.translatable("dye.hexical.uncolored").setStyle(Style.EMPTY.withColor(0xFF00FF))),
	WHITE("white", 0xF9FFFE, DyeColor.WHITE,  Text.translatable("dye.hexical.white").setStyle(Style.EMPTY.withColor(0xF9FFFE))),
	ORANGE("orange", 16351261, DyeColor.ORANGE,  Text.translatable("dye.hexical.orange").setStyle(Style.EMPTY.withColor(16351261))),
	MAGENTA("magenta", 13061821, DyeColor.MAGENTA,  Text.translatable("dye.hexical.magenta").setStyle(Style.EMPTY.withColor(13061821))),
	LIGHT_BLUE("light_blue", 3847130, DyeColor.LIGHT_BLUE,  Text.translatable("dye.hexical.light_blue").setStyle(Style.EMPTY.withColor(3847130))),
	YELLOW("yellow", 16701501, DyeColor.YELLOW,  Text.translatable("dye.hexical.yellow").setStyle(Style.EMPTY.withColor(16701501))),
	LIME("lime", 8439583, DyeColor.LIME,  Text.translatable("dye.hexical.lime").setStyle(Style.EMPTY.withColor(8439583))),
	PINK("pink", 15961002, DyeColor.PINK,  Text.translatable("dye.hexical.pink").setStyle(Style.EMPTY.withColor(15961002))),
	GRAY("gray", 4673362, DyeColor.GRAY,  Text.translatable("dye.hexical.gray").setStyle(Style.EMPTY.withColor(4673362))),
	LIGHT_GRAY("light_gray", 0x9D9D97, DyeColor.LIGHT_GRAY,  Text.translatable("dye.hexical.light_gray").setStyle(Style.EMPTY.withColor(0x9D9D97))),
	CYAN("cyan", 1481884, DyeColor.CYAN,  Text.translatable("dye.hexical.cyan").setStyle(Style.EMPTY.withColor(1481884))),
	PURPLE("purple", 8991416, DyeColor.PURPLE,  Text.translatable("dye.hexical.purple").setStyle(Style.EMPTY.withColor(8991416))),
	BLUE("blue", 3949738, DyeColor.BLUE,  Text.translatable("dye.hexical.blue").setStyle(Style.EMPTY.withColor(3949738))),
	BROWN("brown", 8606770, DyeColor.BROWN,  Text.translatable("dye.hexical.brown").setStyle(Style.EMPTY.withColor(8606770))),
	GREEN("green", 6192150, DyeColor.GREEN,  Text.translatable("dye.hexical.green").setStyle(Style.EMPTY.withColor(6192150))),
	RED("red", 11546150, DyeColor.RED,  Text.translatable("dye.hexical.red").setStyle(Style.EMPTY.withColor(11546150))),
	BLACK("black", 0x1D1D21, DyeColor.BLACK,  Text.translatable("dye.hexical.black").setStyle(Style.EMPTY.withColor(0x1D1D21)));

	companion object {
		fun fromDyeColor(dyeColor: DyeColor?) = when (dyeColor) {
			DyeColor.WHITE -> WHITE
			DyeColor.ORANGE -> ORANGE
			DyeColor.MAGENTA -> MAGENTA
			DyeColor.LIGHT_BLUE -> LIGHT_BLUE
			DyeColor.YELLOW -> YELLOW
			DyeColor.LIME -> LIME
			DyeColor.PINK -> PINK
			DyeColor.GRAY -> GRAY
			DyeColor.LIGHT_GRAY -> LIGHT_GRAY
			DyeColor.CYAN -> CYAN
			DyeColor.PURPLE -> PURPLE
			DyeColor.BLUE -> BLUE
			DyeColor.BROWN -> BROWN
			DyeColor.GREEN -> GREEN
			DyeColor.RED -> RED
			DyeColor.BLACK -> BLACK
			null -> UNCOLORED
		}
	}
}