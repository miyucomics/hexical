package miyucomics.hexical.iota

import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.IotaType
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
			override fun color() = -0x2155e3
			override fun display(tag: NbtElement): Text {
				val compound = (tag as NbtCompound)
				return Text.literal(compound.getString("namespace") + ":" + compound.getString("path")).styledWith(Formatting.GOLD)
			}

			override fun deserialize(tag: NbtElement?, world: ServerWorld?): IdentifierIota {
				val compound = (tag!! as NbtCompound)
				return IdentifierIota(Identifier(compound.getString("namespace"), compound.getString("path")))
			}
		}
	}
}