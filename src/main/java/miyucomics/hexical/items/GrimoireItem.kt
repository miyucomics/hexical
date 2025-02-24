package miyucomics.hexical.items

import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World

class GrimoireItem : Item(Settings().maxCount(1)) {
	override fun appendTooltip(stack: ItemStack, world: World?, list: MutableList<Text>, tooltipContext: TooltipContext) {
		if (!stack.hasNbt()) {
			super.appendTooltip(stack, world, list, tooltipContext)
			return
		}

		val metadata = stack.nbt!!.getCompound("metadata")
		val text = Text.translatable("hexical.grimoire.contains")
		for (key in metadata.keys) {
			val data = metadata.getCompound(key)
			text.append(PatternIota(HexPattern.fromAngles(key, HexDir.values()[data.getInt("direction")])).display())
		}

		list.add(text.formatted(Formatting.GRAY))
		super.appendTooltip(stack, world, list, tooltipContext)
	}
}