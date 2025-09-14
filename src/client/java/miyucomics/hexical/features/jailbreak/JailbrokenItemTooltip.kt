package miyucomics.hexical.features.jailbreak

import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import miyucomics.hexical.features.charms.CharmUtilities
import miyucomics.hexical.features.curios.CurioItem
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object JailbrokenItemTooltip : InitHook() {
	override fun init() {
		ItemTooltipCallback.EVENT.register { stack, _, lines ->
			val nbt = stack.nbt ?: return@register
			if (stack.item !is ItemPackagedHex || !nbt.getBoolean("cracked"))
				return@register
			if (nbt.contains(ItemPackagedHex.TAG_PROGRAM))
				lines.add("hexical.cracked.hex".asTranslatedComponent(getText(nbt.getList(ItemPackagedHex.TAG_PROGRAM, NbtElement.COMPOUND_TYPE.toInt()))))
			else
				lines.add("hexical.cracked.cracked".asTranslatedComponent.formatted(Formatting.GOLD))
		}

		ItemTooltipCallback.EVENT.register { stack, _, lines ->
			val nbt = stack.nbt ?: return@register
			if (stack.item !is CurioItem || !nbt.getBoolean("cracked"))
				return@register
			if (CharmUtilities.isStackCharmed(stack))
				lines.add("hexical.cracked.hex".asTranslatedComponent(getText(CharmUtilities.getCompound(stack).getList("hex", NbtElement.COMPOUND_TYPE.toInt()))))
			else
				lines.add("hexical.cracked.cracked".asTranslatedComponent.formatted(Formatting.GOLD))
		}
	}

	private fun getText(hex: NbtList) = hex.fold(Text.empty()) { acc, curr -> acc.append(IotaType.getDisplay(curr.asCompound)) }
}