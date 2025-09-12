package miyucomics.hexical.features.dyes

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.utils.asInt
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtInt
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.DyeColor

class DyeIota(dye: DyeOption) : Iota(TYPE, dye) {
	override fun isTruthy() = true
	val dye: DyeOption = this.payload as DyeOption
	override fun toleratesOther(that: Iota) = (typesMatch(this, that) && that is DyeIota) && this.dye == that.dye
	override fun serialize(): NbtElement = NbtInt.of(dye.ordinal)

	companion object {
		var TYPE: IotaType<DyeIota> = object : IotaType<DyeIota>() {
			override fun color() = -1
			override fun deserialize(tag: NbtElement, world: ServerWorld) = DyeIota(enumValues<DyeOption>()[tag.asInt])
			override fun display(tag: NbtElement): Text = enumValues<DyeOption>()[tag.asInt].coloredText
		}
	}
}

fun List<Iota>.getDye(idx: Int, argc: Int = 0): DyeOption {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is DyeIota)
		return x.dye
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "dye")
}

fun List<Iota>.getColoredDye(idx: Int, argc: Int = 0): DyeColor {
	val dye = getDye(idx, argc)
	if (dye != DyeOption.UNCOLORED)
		return dye.dyeColor!!
	throw MishapInvalidIota.of(this[idx], if (argc == 0) idx else argc - (idx + 1), "colored_dye")
}