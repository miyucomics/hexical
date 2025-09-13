package miyucomics.hexical.features.pigments

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.misc.TextUtilities.getPigmentedText
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text

class PigmentIota(pigment: FrozenPigment) : Iota(TYPE, pigment) {
	override fun isTruthy() = true
	val pigment: FrozenPigment = this.payload as FrozenPigment
	override fun toleratesOther(that: Iota) = typesMatch(this, that) && this.pigment == (that as PigmentIota).pigment

	override fun serialize(): NbtElement {
		val compound = NbtCompound()
		compound.putCompound("pigment", pigment.serializeToNBT())
		compound.putString("name", pigment.item.translationKey)
		return compound
	}

	companion object {
		val TYPE: IotaType<PigmentIota> = object : IotaType<PigmentIota>() {
			override fun color() = 0xff_c466e3.toInt()
			override fun deserialize(tag: NbtElement, world: ServerWorld) = PigmentIota(FrozenPigment.fromNBT((tag as NbtCompound).getCompound("pigment")))
			override fun display(tag: NbtElement): Text {
				val compound = tag as NbtCompound
				val name = Text.translatable(compound.getString("name")).string
				return getPigmentedText(name, FrozenPigment.fromNBT(compound.getCompound("pigment")))
			}
		}
	}
}

fun List<Iota>.getPigment(idx: Int, argc: Int = 0): FrozenPigment {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is PigmentIota)
		return x.pigment
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "pigment")
}