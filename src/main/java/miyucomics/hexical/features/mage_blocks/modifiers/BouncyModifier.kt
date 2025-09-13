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

class BouncyModifier : MageBlockModifier {
	override val type: MageBlockModifierType<*> = TYPE

	override fun getScryingLens(): Pair<ItemStack, Text> = Pair(ItemStack(Items.SLIME_BALL), "hexical.mage_block.bouncy".asTranslatedComponent)
	override fun serialize(): NbtElement = NbtByte.of(false)

	companion object {
		val TYPE: MageBlockModifierType<BouncyModifier> = object : MageBlockModifierType<BouncyModifier>() {
			override val argc: Int = 0
			override val id = HexicalMain.id("bouncy")
			override fun construct(args: List<Iota>) = BouncyModifier()
			override fun deserialize(element: NbtElement) = BouncyModifier()
		}
	}
}