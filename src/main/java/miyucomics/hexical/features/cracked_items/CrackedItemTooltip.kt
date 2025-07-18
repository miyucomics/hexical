package miyucomics.hexical.features.cracked_items

import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import miyucomics.hexical.features.charms.CharmUtilities
import miyucomics.hexical.features.charms.CharmUtilities.getCompound
import miyucomics.hexical.features.curios.CurioItem
import miyucomics.hexical.inits.InitHook
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object CrackedItemTooltip : InitHook() {
	override fun init() {
		ItemTooltipCallback.EVENT.register { stack, _, lines ->
			val nbt = stack.nbt ?: return@register
			if (stack.item !is ItemPackagedHex || !nbt.getBoolean("cracked"))
				return@register

			lines.add(Text.translatable("hexical.cracked.cracked").formatted(Formatting.GOLD))
			if (nbt.contains(ItemPackagedHex.TAG_PROGRAM)) {
				val text = Text.empty()
				val entries = nbt.getList(ItemPackagedHex.TAG_PROGRAM, NbtElement.COMPOUND_TYPE.toInt())
				entries.forEach { text.append(IotaType.getDisplay(it as NbtCompound)) }
				lines.add(Text.translatable("hexical.cracked.program").append(text))
			}
		}

		ItemTooltipCallback.EVENT.register { stack, _, lines ->
			val nbt = stack.nbt ?: return@register
			if (stack.item !is CurioItem || !nbt.getBoolean("cracked"))
				return@register

			lines.add(Text.translatable("hexical.cracked.cracked").formatted(Formatting.GOLD))
			if (CharmUtilities.isStackCharmed(stack))
				lines.add(Text.translatable("hexical.cracked.program").append(IotaType.getDisplay(getCompound(stack).getCompound("instructions"))))
		}
	}
}