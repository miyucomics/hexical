package miyucomics.hexical.features.mage_blocks.modifiers

import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.mage_blocks.MageBlockModifier
import miyucomics.hexical.features.mage_blocks.MageBlockModifierType
import net.minecraft.nbt.NbtByte
import net.minecraft.nbt.NbtElement

class ReplaceableModifier : MageBlockModifier {
	override val type: MageBlockModifierType<*> = TYPE

	override fun serialize(): NbtElement = NbtByte.of(false)

	companion object {
		var TYPE: MageBlockModifierType<ReplaceableModifier> = object : MageBlockModifierType<ReplaceableModifier>() {
			override val argc: Int = 0
			override val id = HexicalMain.id("replaceable")
			override fun construct(args: List<Iota>) = ReplaceableModifier()
			override fun deserialize(element: NbtElement) = ReplaceableModifier()
		}
	}
}