package miyucomics.hexical.registry

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.utils.mediaBarColor
import at.petrak.hexcasting.common.items.magic.ItemMediaHolder
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import at.petrak.hexcasting.fabric.event.MouseScrollCallback
import at.petrak.hexcasting.xplat.IXplatAbstractions
import com.google.common.math.LinearTransformation.vertical
import com.mojang.blaze3d.systems.RenderSystem
import miyucomics.hexical.casting.components.LedgerRecordComponent
import miyucomics.hexical.casting.components.SentinelBedComponent
import miyucomics.hexical.client.ClientStorage
import miyucomics.hexical.client.ShaderRenderer
import miyucomics.hexical.data.EvokeState
import miyucomics.hexical.data.KeybindData
import miyucomics.hexical.data.LesserSentinelState
import miyucomics.hexical.interfaces.PlayerEntityMinterface
import miyucomics.hexical.utils.CharmedItemUtilities.getMaxMedia
import miyucomics.hexical.utils.CharmedItemUtilities.getMedia
import miyucomics.hexical.utils.CharmedItemUtilities.isStackCharmed
import miyucomics.hexical.utils.RenderUtils
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.util.Formatting
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import java.awt.SystemColor.window
import java.util.function.Consumer
import kotlin.math.cos
import kotlin.math.sin

object HexicalEvents {
	val CHARMED_COLOR: TextColor = TextColor.fromRgb(0xe83d72)

	@JvmStatic
	fun init() {
		LesserSentinelState.registerServerReciever()

		CastingEnvironment.addCreateEventListener { env: CastingEnvironment, _: NbtCompound ->
			env.addExtension(SentinelBedComponent(env))
			if (env is PlayerBasedCastEnv)
				env.addExtension(LedgerRecordComponent(env))
		}

		ServerPlayerEvents.AFTER_RESPAWN.register { oldPlayer, newPlayer, alive ->
			if (!alive)
				ShaderRenderer.setEffect(null)
			(newPlayer as PlayerEntityMinterface).setLesserSentinels((oldPlayer as PlayerEntityMinterface).getLesserSentinels())
			(newPlayer as PlayerEntityMinterface).setEvocation((oldPlayer as PlayerEntityMinterface).getEvocation())
			(newPlayer as PlayerEntityMinterface).setWristpocket((oldPlayer as PlayerEntityMinterface).getWristpocket())
		}

		ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
			val player = handler.player.uuid
			EvokeState.active[player] = false
			if (KeybindData.active.containsKey(player)) {
				for (key in KeybindData.active[player]!!.keys) {
					KeybindData.active[player]!![key] = false
					KeybindData.duration[player]!![key] = 0
				}
			}
		}

		ServerTickEvents.END_SERVER_TICK.register {
			for (player in EvokeState.active.keys)
				if (EvokeState.active[player]!!)
					EvokeState.duration[player] = EvokeState.duration[player]!! - 1
			for (player in KeybindData.duration.keys) {
				val binds = KeybindData.active[player]!!
				for (key in binds.keys)
					if (KeybindData.active[player]!!.getOrDefault(key, false))
						KeybindData.duration[player]!![key] = KeybindData.duration[player]!![key]!! + 1
			}
		}
	}

	@JvmStatic
	fun clientInit() {
		MouseScrollCallback.EVENT.register { delta ->
			if (HexicalKeybinds.TELEPATHY_KEYBIND.isPressed) {
				val buf = PacketByteBufs.create()
				buf.writeInt(delta.toInt())
				ClientPlayNetworking.send(HexicalNetworking.SCROLL_CHANNEL, buf)
				return@register true
			}
			return@register false
		}

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
		WorldRenderEvents.LAST.register { ctx ->
			ClientStorage.lesserSentinels.forEach { pos ->
				val matrices = ctx.matrixStack()
				val camera = ctx.camera()
				val camPos = camera.pos

				matrices.push()
				matrices.translate(pos.x - camPos.x, pos.y - camPos.y, pos.z - camPos.z)

				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.yaw))
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.pitch))

				val tessellator = Tessellator.getInstance()
				val bufferBuilder = tessellator.buffer

				RenderSystem.disableDepthTest()
				RenderSystem.enableBlend()
				RenderSystem.defaultBlendFunc()
				RenderSystem.disableCull()
				RenderSystem.setShader(GameRenderer::getPositionColorProgram)

				bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)

				val points = mutableListOf<Vec2f>()
				for (i in 0..6) {
					val angle = (i % 6) * (Math.PI / 3)
					points.add(Vec2f(cos(angle).toFloat(), sin(angle).toFloat()).multiply(0.25f))
				}

				val pigment = IXplatAbstractions.INSTANCE.getPigment(MinecraftClient.getInstance().player!!).colorProvider
				fun makeVertex(offset: Vec2f) = bufferBuilder.vertex(matrices.peek().positionMatrix, offset.x, offset.y, 0f)
					.color(pigment.getColor(ClientStorage.ticks.toFloat(), pos.add(offset.x.toDouble() * 2, offset.y.toDouble() * 2, 0.0)))
					.next()
				RenderUtils.quadifyLines(::makeVertex, 0.05f, points)

				tessellator.draw()

				RenderSystem.enableCull()
				RenderSystem.disableBlend()
				RenderSystem.enableDepthTest()

				matrices.pop()
			}
		}
	}
}