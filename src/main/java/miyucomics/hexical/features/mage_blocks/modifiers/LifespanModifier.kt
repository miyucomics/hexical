package miyucomics.hexical.features.mage_blocks.modifiers

import at.petrak.hexcasting.api.casting.getPositiveInt
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.utils.asInt
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import com.mojang.datafixers.util.Pair
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.mage_blocks.MageBlockModifier
import miyucomics.hexical.features.mage_blocks.MageBlockModifierType
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtInt
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class LifespanModifier : MageBlockModifier {
	override val type: MageBlockModifierType<*> = TYPE
	var lifespan = 0

	override fun getScryingLens(): Pair<ItemStack, Text> = Pair(ItemStack(Items.CLOCK), "hexical.mage_block.lifespan".asTranslatedComponent(lifespan / 20f))
	override fun serialize(): NbtElement = NbtInt.of(lifespan)
	override fun tick(world: World, pos: BlockPos, state: BlockState) {
		lifespan--
		if (lifespan <= 0)
			HexicalBlocks.MAGE_BLOCK.onBreak(world, pos, state, null)
	}

	companion object {
		var TYPE: MageBlockModifierType<LifespanModifier> = object : MageBlockModifierType<LifespanModifier>() {
			override val argc: Int = 1
			override val id = HexicalMain.id("lifespan")
			override fun construct(args: List<Iota>) = LifespanModifier().also { it.lifespan = args.getPositiveInt(1, 2) }
			override fun deserialize(element: NbtElement) = LifespanModifier().also { it.lifespan = element.asInt }
		}
	}
}