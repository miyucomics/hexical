package miyucomics.hexical.features.charms

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.mediaBarColor
import at.petrak.hexcasting.common.items.magic.ItemMediaHolder
import miyucomics.hexical.misc.DecimalFormats
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.minecraft.text.Text
import net.minecraft.text.TextColor

object CharmedItemTooltip : InitHook() {
	override fun init() {
		ItemTooltipCallback.EVENT.register { stack, _, lines ->
			if (!CharmUtilities.isStackCharmed(stack))
				return@register
			val media = CharmUtilities.getMedia(stack)
			val maxMedia = CharmUtilities.getMaxMedia(stack)
			lines.add(Text.translatable("hexical.charmed").styled { style -> style.withColor(CharmUtilities.CHARMED_COLOR) })
			lines.add(
				Text.translatable("hexcasting.tooltip.media_amount.advanced",
				Text.literal(DecimalFormats.DUST_AMOUNT.format((media / MediaConstants.DUST_UNIT.toFloat()).toDouble())).styled { style -> style.withColor(
					ItemMediaHolder.HEX_COLOR) },
				Text.translatable("hexcasting.tooltip.media", DecimalFormats.DUST_AMOUNT.format((maxMedia / MediaConstants.DUST_UNIT.toFloat()).toDouble())).styled { style -> style.withColor(
					ItemMediaHolder.HEX_COLOR) },
				Text.literal(DecimalFormats.PERCENTAGE.format((100f * media / maxMedia).toDouble()) + "%").styled { style -> style.withColor(
					TextColor.fromRgb(mediaBarColor(media, maxMedia))) }
			))
		}
	}
}