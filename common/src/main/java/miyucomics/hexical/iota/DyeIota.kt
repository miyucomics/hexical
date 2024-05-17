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
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import java.util.*

class DyeIota(color: DyeColor) : Iota(HexicalIota.DYE_IOTA, color.name) {
	override fun isTruthy() = true
	val dye: DyeColor = DyeColor.byName(this.payload as String, DyeColor.BLACK)!!
	override fun toleratesOther(that: Iota) = (typesMatch(this, that) && that is DyeIota) && this.dye == that.dye

	override fun serialize(): NbtElement {
		val compound = NbtCompound()
		compound.putString("color", dye.name)
		return compound
	}

	companion object {
		var TYPE: IotaType<DyeIota> = object : IotaType<DyeIota>() {
			override fun color() = 0xff_e6c24c.toInt()
			override fun deserialize(tag: NbtElement?, world: ServerWorld?) = DyeIota(DyeColor.byName((tag!! as NbtCompound).getString("color"), DyeColor.BLACK)!!)
			override fun display(tag: NbtElement) = Text.literal((tag as NbtCompound).getString("color").replace("_", " "))
		}
	}
}

fun List<Iota>.getDye(idx: Int, argc: Int = 0): DyeColor {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is DyeIota)
		return x.dye
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "dye")
}