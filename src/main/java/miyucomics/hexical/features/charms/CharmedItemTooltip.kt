package miyucomics.hexical.features.charms

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.mediaBarColor
import at.petrak.hexcasting.common.items.magic.ItemMediaHolder
import miyucomics.hexical.features.charms.CharmedItemUtilities.CHARMED_COLOR
import miyucomics.hexical.features.charms.CharmedItemUtilities.getMaxMedia
import miyucomics.hexical.features.charms.CharmedItemUtilities.getMedia
import miyucomics.hexical.features.charms.CharmedItemUtilities.isStackCharmed
import miyucomics.hexical.inits.InitHook
import miyucomics.hexical.misc.RenderUtils
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.minecraft.text.Text
import net.minecraft.text.TextColor

object CharmedItemTooltip : InitHook() {
	override fun init() {
		ItemTooltipCallback.EVENT.register { stack, _, lines ->
			if (!isStackCharmed(stack))
				return@register
			val media = getMedia(stack)
			val maxMedia = getMaxMedia(stack)
			lines.add(Text.translatable("hexical.charmed").styled { style -> style.withColor(CHARMED_COLOR) })
			lines.add(Text.translatable("hexcasting.tooltip.media_amount.advanced",
				Text.literal(RenderUtils.DUST_AMOUNT.format((media / MediaConstants.DUST_UNIT.toFloat()).toDouble())).styled { style -> style.withColor(ItemMediaHolder.HEX_COLOR) },
				Text.translatable("hexcasting.tooltip.media", RenderUtils.DUST_AMOUNT.format((maxMedia / MediaConstants.DUST_UNIT.toFloat()).toDouble())).styled { style -> style.withColor(ItemMediaHolder.HEX_COLOR) },
				Text.literal(RenderUtils.PERCENTAGE.format((100f * media / maxMedia).toDouble()) + "%").styled { style -> style.withColor(TextColor.fromRgb(mediaBarColor(media, maxMedia))) }
			))
		}
	}
}