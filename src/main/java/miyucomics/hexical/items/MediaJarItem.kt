package miyucomics.hexical.items

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.mediaBarColor
import at.petrak.hexcasting.common.items.magic.ItemMediaHolder
import miyucomics.hexical.blocks.MediaJarBlock
import miyucomics.hexical.registry.HexicalBlocks
import miyucomics.hexical.utils.RenderUtils
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.world.World

class MediaJarItem : BlockItem(HexicalBlocks.MEDIA_JAR_BLOCK, Settings().maxCount(1)) {
	override fun appendTooltip(stack: ItemStack, world: World?, list: MutableList<Text>, tooltipContext: TooltipContext) {
		val tag = stack.nbt?.getCompound("BlockEntityTag")
		val media = tag?.getLong("media") ?: 0
		list.add(Text.translatable("hexcasting.tooltip.media_amount.advanced",
			Text.literal(RenderUtils.DUST_AMOUNT.format((media / MediaConstants.DUST_UNIT.toFloat()).toDouble())).styled { style -> style.withColor(ItemMediaHolder.HEX_COLOR) },
			Text.translatable("hexcasting.tooltip.media", RenderUtils.DUST_AMOUNT.format((MediaJarBlock.MAX_CAPACITY / MediaConstants.DUST_UNIT.toFloat()).toDouble())).styled { style -> style.withColor(ItemMediaHolder.HEX_COLOR) },
			Text.literal(RenderUtils.PERCENTAGE.format((100f * media / MediaJarBlock.MAX_CAPACITY).toDouble()) + "%").styled { style -> style.withColor(TextColor.fromRgb(mediaBarColor(media, MediaJarBlock.MAX_CAPACITY))) }
		))
	}
}