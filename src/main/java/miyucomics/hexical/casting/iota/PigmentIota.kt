package miyucomics.hexical.casting.iota

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.IotaType
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.registry.HexicalIota
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text

class PigmentIota(pigment: FrozenColorizer) : Iota(HexicalIota.PIGMENT_IOTA, pigment) {
	override fun isTruthy() = true
	val pigment: FrozenColorizer = this.payload as FrozenColorizer
	override fun toleratesOther(that: Iota) = typesMatch(this, that) && this.pigment == (that as PigmentIota).pigment

	override fun serialize(): NbtElement {
		val compound = NbtCompound()
		compound.putCompound("pigment", pigment.serializeToNBT())
		compound.putString("name", pigment.item.name.toString())
		return compound
	}

	companion object {
		var TYPE: IotaType<PigmentIota> = object : IotaType<PigmentIota>() {
			override fun color() = 0xff_c466e3.toInt()
			override fun deserialize(tag: NbtElement, world: ServerWorld) = PigmentIota(FrozenColorizer.fromNBT((tag as NbtCompound).getCompound("pigment")))
			override fun display(tag: NbtElement) = Text.literal((tag as NbtCompound).getString("name"))
		}
	}
}

fun List<Iota>.getPigment(idx: Int, argc: Int = 0): FrozenColorizer {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is PigmentIota)
		return x.pigment
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "pigment")
}