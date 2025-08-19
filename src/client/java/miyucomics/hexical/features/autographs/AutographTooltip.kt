package miyucomics.hexical.features.autographs

import at.petrak.hexcasting.api.pigment.FrozenPigment
import miyucomics.hexical.ClientStorage
import miyucomics.hexical.misc.InitHook
import miyucomics.hexical.misc.TextUtilities
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.function.Consumer

object AutographTooltip : InitHook() {
	override fun init() {
		ItemTooltipCallback.EVENT.register { stack, _, lines ->
			val nbt = stack.nbt ?: return@register
			if (!nbt.contains("autographs"))
				return@register

			lines.add(Text.translatable("hexical.autograph.header").styled { style -> style.withColor(Formatting.GRAY) })

			nbt.getList("autographs", NbtCompound.COMPOUND_TYPE.toInt()).forEach(Consumer { element: NbtElement? ->
				val compound = element as NbtCompound
				lines.add(TextUtilities.getPigmentedText(compound.getString("name"), FrozenPigment.fromNBT(compound.getCompound("pigment")), offset = ClientStorage.ticks * 3f))
			})
		}
	}
}