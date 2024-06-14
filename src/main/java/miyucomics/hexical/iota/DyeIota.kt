package miyucomics.hexical.iota

import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.IotaType
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.utils.styledWith
import miyucomics.hexical.registry.HexicalIota
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import java.util.*

class DyeIota(color: String) : Iota(HexicalIota.DYE_IOTA, color) {
	override fun isTruthy() = true
	val dye: String = this.payload as String
	override fun toleratesOther(that: Iota) = (typesMatch(this, that) && that is DyeIota) && this.dye == that.dye

	override fun serialize(): NbtElement {
		val compound = NbtCompound()
		compound.putString("color", dye)
		return compound
	}

	companion object {
		private val MAP = mapOf(
			"white" to 0xF9FFFE,
			"orange" to 16351261,
			"magenta" to 13061821,
			"light_blue" to 3847130,
			"yellow" to 16701501,
			"lime" to 8439583,
			"pink" to 15961002,
			"gray" to 4673362,
			"light_gray" to 0x9D9D97,
			"cyan" to 1481884,
			"purple" to 8991416,
			"blue" to 3949738,
			"brown" to 8606770,
			"green" to 6192150,
			"red" to 11546150,
			"black" to 0x1D1D21,
			"uncolored" to 0xff_ff00ff.toInt(),
		)

		var TYPE: IotaType<DyeIota> = object : IotaType<DyeIota>() {
			override fun color() = 0xff_ffffff.toInt()
			override fun deserialize(tag: NbtElement?, world: ServerWorld?) = DyeIota((tag!! as NbtCompound).getString("color"))
			override fun display(tag: NbtElement): Text {
				val color = (tag as NbtCompound).getString("color")
				return Text.literal(color.replace("_", " ")).styledWith(Style.EMPTY.withColor(MAP[color]!!))
			}
		}
	}
}

fun List<Iota>.getTrueDye(idx: Int, argc: Int = 0): DyeColor {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is DyeIota && x.dye != "uncolored")
		return DyeColor.byName(x.dye, DyeColor.BLACK)!!
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "uncolored_dye")
}

fun List<Iota>.getDye(idx: Int, argc: Int = 0): String {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is DyeIota)
		return x.dye
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "dye")
}