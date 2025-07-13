package miyucomics.hexical.features.player.fields

import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.api.utils.asDouble
import at.petrak.hexcasting.api.utils.putList
import at.petrak.hexcasting.xplat.IXplatAbstractions
import com.mojang.blaze3d.systems.RenderSystem
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.misc.ClientStorage
import miyucomics.hexical.features.player.PlayerField
import miyucomics.hexical.features.player.getHexicalPlayerManager
import miyucomics.hexical.misc.RenderUtils
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtDouble
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.math.cos
import kotlin.math.sin

class LesserSentinelField : PlayerField {
	var instances: HashMap<RegistryKey<World>, DimensionalLesserSentinelInstance> = hashMapOf()

	override fun readNbt(compound: NbtCompound) {
		instances.clear()
		if (!compound.contains("lesser_sentinels"))
			return
		compound.getList("lesser_sentinels", NbtElement.COMPOUND_TYPE.toInt()).forEach {
			val instance = DimensionalLesserSentinelInstance.createFromNbt(it.asCompound)
			instances[instance.dimension] = instance
		}
	}

	override fun writeNbt(compound: NbtCompound) {
		compound.putList("lesser_sentinels", NbtList().also {
			instances.values.forEach { instance -> it.add(instance.toNbt()) }
		})
	}

	fun getCurrentInstance(player: ServerPlayerEntity) = instances.getOrPut(player.serverWorld.registryKey) { DimensionalLesserSentinelInstance(mutableListOf(), player.serverWorld.registryKey) }

	fun syncToClient(player: ServerPlayerEntity) {
		val buf = PacketByteBufs.create()
		val instance = getCurrentInstance(player)
		buf.writeInt(instance.lesserSentinels.size)
		instance.lesserSentinels.forEach { pos ->
			buf.writeDouble(pos.x)
			buf.writeDouble(pos.y)
			buf.writeDouble(pos.z)
		}
		ServerPlayNetworking.send(player, LESSER_SENTINEL_CHANNEL, buf)
	}

	companion object {
		private val LESSER_SENTINEL_CHANNEL = HexicalMain.id("lesser_sentinels")

		fun registerServerCallbacks() {
			ServerPlayConnectionEvents.JOIN.register { handler, _, server -> handler.player.syncLesserSentinels() }
			ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register { player, origin, destination -> player.syncLesserSentinels() }
		}

		fun registerClientCallbacks() {
			ClientPlayNetworking.registerGlobalReceiver(LESSER_SENTINEL_CHANNEL) { client, _, packet, _ ->
				val count = packet.readInt()
				val list = mutableListOf<Vec3d>()

				repeat(count) {
					val x = packet.readDouble()
					val y = packet.readDouble()
					val z = packet.readDouble()
					list.add(Vec3d(x, y, z))
				}

				client.execute {
					ClientStorage.lesserSentinels.clear()
					ClientStorage.lesserSentinels.addAll(list)
				}
			}

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
}

data class DimensionalLesserSentinelInstance(var lesserSentinels: MutableList<Vec3d>, val dimension: RegistryKey<World>) {
	fun toNbt(): NbtCompound {
		val compound = NbtCompound()

		val location = NbtList()
		lesserSentinels.forEach { pos ->
			location.add(NbtDouble.of(pos.x))
			location.add(NbtDouble.of(pos.y))
			location.add(NbtDouble.of(pos.z))
		}

		compound.putString("dimension", dimension.value.toString())
		compound.putList("positional", location)
		return compound
	}

	companion object {
		fun createFromNbt(compound: NbtCompound): DimensionalLesserSentinelInstance {
			val lesserSentinels = mutableListOf<Vec3d>()
			val positions = compound.getList("positional", NbtElement.DOUBLE_TYPE.toInt()).toMutableList()
			while (positions.isNotEmpty())
				lesserSentinels.add(Vec3d(positions.removeFirst().asDouble, positions.removeFirst().asDouble, positions.removeFirst().asDouble))
			return DimensionalLesserSentinelInstance(lesserSentinels, RegistryKey.of(RegistryKeys.WORLD, Identifier(compound.getString("dimension"))))
		}
	}
}

fun ServerPlayerEntity.getLesserSentinels() = this.getHexicalPlayerManager().get(LesserSentinelField::class)
fun ServerPlayerEntity.syncLesserSentinels() = this.getLesserSentinels().syncToClient(this)