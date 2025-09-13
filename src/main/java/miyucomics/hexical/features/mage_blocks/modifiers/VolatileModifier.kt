package miyucomics.hexical.features.mage_blocks.modifiers

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import com.mojang.datafixers.util.Pair
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.mage_blocks.MageBlockModifier
import miyucomics.hexical.features.mage_blocks.MageBlockModifierType
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtByte
import net.minecraft.nbt.NbtElement
import net.minecraft.text.Text

class VolatileModifier : MageBlockModifier {
	override val type: MageBlockModifierType<*> = TYPE

	override fun getScryingLens(): Pair<ItemStack, Text> = Pair(ItemStack(Items.TNT), "hexical.mage_block.volatile".asTranslatedComponent)
	override fun serialize(): NbtElement = NbtByte.of(false)

	companion object {
		val TYPE: MageBlockModifierType<VolatileModifier> = object : MageBlockModifierType<VolatileModifier>() {
			override val argc: Int = 0
			override val id = HexicalMain.id("volatile")
			override fun construct(args: List<Iota>) = VolatileModifier()
			override fun deserialize(element: NbtElement) = VolatileModifier()
		}
	}
}