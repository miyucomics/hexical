package miyucomics.hexical.features.mage_blocks.modifiers

import at.petrak.hexcasting.api.casting.getPositiveIntUnderInclusive
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.utils.asInt
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import com.mojang.datafixers.util.Pair
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.mage_blocks.MageBlockModifier
import miyucomics.hexical.features.mage_blocks.MageBlockModifierType
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtInt
import net.minecraft.text.Text

class RedstoneModifier : MageBlockModifier {
	override val type: MageBlockModifierType<*> = TYPE
	var power = 0

	override fun getScryingLens(): Pair<ItemStack, Text> = Pair(ItemStack(Items.REDSTONE), "hexical.mage_block.redstone".asTranslatedComponent(power))
	override fun serialize(): NbtElement = NbtInt.of(power)

	companion object {
		val TYPE: MageBlockModifierType<RedstoneModifier> = object : MageBlockModifierType<RedstoneModifier>() {
			override val argc: Int = 1
			override val id = HexicalMain.id("redstone")
			override fun construct(args: List<Iota>) = RedstoneModifier().also { it.power = args.getPositiveIntUnderInclusive(1, 15, 2) }
			override fun deserialize(element: NbtElement) = RedstoneModifier().also { it.power = element.asInt }
		}
	}
}