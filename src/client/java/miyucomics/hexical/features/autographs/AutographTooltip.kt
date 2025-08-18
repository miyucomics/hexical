package miyucomics.hexical.features.autographs

import at.petrak.hexcasting.api.pigment.FrozenPigment
import miyucomics.hexical.ClientStorage
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.Vec3d
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
				val name = compound.getString("name")
				val pigment = FrozenPigment.fromNBT(compound.getCompound("pigment")).colorProvider
				val output = Text.literal("")
				for (i in 0 until name.length)
					output.append(
						Text.literal(name[i].toString()).styled { style ->
							style.withColor(
								pigment.getColor(
									(ClientStorage.ticks * 3).toFloat(),
									Vec3d(0.0, i.toDouble(), 0.0)
								)
							)
						})
				lines.add(output)
			})
		}
	}
}