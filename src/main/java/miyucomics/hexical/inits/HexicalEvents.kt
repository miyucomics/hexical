package miyucomics.hexical.inits

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import miyucomics.hexical.casting.components.SentinelBedComponent
import miyucomics.hexical.misc.ClientStorage
import miyucomics.hexical.misc.ShaderRenderer
import miyucomics.hexical.features.charms.CharmedItemUtilities
import miyucomics.hexical.features.player.fields.MediaLogField
import miyucomics.hexical.features.player.fields.LesserSentinelField
import miyucomics.hexical.features.player.getHexicalPlayerManager
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.Vec3d
import java.util.function.Consumer

object HexicalEvents {
	fun init() {
		LesserSentinelField.registerServerCallbacks()
		MediaLogField.registerServerCallbacks()

		CastingEnvironment.addCreateEventListener { env: CastingEnvironment, _: NbtCompound ->
			env.addExtension(SentinelBedComponent(env))
		}

		ServerPlayerEvents.AFTER_RESPAWN.register { old, new, alive ->
			if (!alive)
				ShaderRenderer.setEffect(null)
			new.getHexicalPlayerManager().handleRespawn(new, old)
		}
	}

	fun clientInit() {
		CharmedItemUtilities.registerClientCallbacks()

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
			if (!nbt.contains("autographs"))
				return@register

			lines.add(Text.translatable("hexical.autograph.header").styled { style -> style.withColor(Formatting.GRAY) })

			nbt.getList("autographs", NbtCompound.COMPOUND_TYPE.toInt()).forEach(Consumer { element: NbtElement? ->
				val compound = element as NbtCompound
				val name = compound.getString("name")
				val pigment = FrozenPigment.fromNBT(compound.getCompound("pigment")).colorProvider
				val output = Text.literal("")
				for (i in 0 until name.length)
					output.append(Text.literal(name[i].toString()).styled { style -> style.withColor(pigment.getColor((ClientStorage.ticks * 3).toFloat(), Vec3d(0.0, i.toDouble(), 0.0))) })
				lines.add(output)
			})
		}

		ClientPlayConnectionEvents.DISCONNECT.register { _, _ -> ShaderRenderer.setEffect(null) }
		ClientTickEvents.END_CLIENT_TICK.register { ClientStorage.ticks += 1 }
	}
}