package miyucomics.hexical.features.cracked_items

import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import miyucomics.hexical.inits.Hook
import miyucomics.hexical.misc.ClientStorage
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.Vec3d
import java.util.function.Consumer

object CrackedItemTooltip : Hook() {
	override fun registerCallbacks() {
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
	}
}