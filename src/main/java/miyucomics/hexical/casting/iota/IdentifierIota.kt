package miyucomics.hexical.casting.iota

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
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

class IdentifierIota(identifier: Identifier) : Iota(HexicalIota.IDENTIFIER_IOTA, identifier) {
	override fun isTruthy() = true
	val identifier = this.payload as Identifier
	override fun toleratesOther(that: Iota) = (typesMatch(this, that) && that is IdentifierIota) && this.identifier == that.identifier

	override fun serialize(): NbtElement {
		val compound = NbtCompound()
		compound.putString("namespace", identifier.namespace)
		compound.putString("path", identifier.path)
		return compound
	}

	companion object {
		var TYPE: IotaType<IdentifierIota> = object : IotaType<IdentifierIota>() {
			override fun color() = 0xff_e6c24c.toInt()
			override fun display(tag: NbtElement): Text {
				val compound = (tag as NbtCompound)
				return Text.literal(compound.getString("namespace") + ":" + compound.getString("path")).styledWith(Formatting.GOLD)
			}

			override fun deserialize(tag: NbtElement, world: ServerWorld): IdentifierIota {
				val compound = (tag as NbtCompound)
				return IdentifierIota(Identifier(compound.getString("namespace"), compound.getString("path")))
			}
		}
	}
}

fun Identifier.asActionResult(): List<Iota> = listOf(IdentifierIota(this))

fun List<Iota>.getIdentifier(idx: Int, argc: Int = 0): Identifier {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is IdentifierIota)
		return x.identifier
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "identifier")
}