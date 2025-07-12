package miyucomics.hexical.blocks

import miyucomics.hexical.utils.MediaJarRenderStuffs
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import org.joml.Vector3f

class MediaJarBlockEntityRenderer : BlockEntityRenderer<MediaJarBlockEntity> {
	override fun render(jar: MediaJarBlockEntity, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {
		MediaJarRenderStuffs.renderFluid(matrices, vertexConsumers, jar.getMedia().toFloat() / MediaJarBlock.MAX_CAPACITY.toFloat(), hashBlockPos(jar.pos))
	}

	companion object {
		fun hashBlockPos(pos: BlockPos): Vector3f {
			var x = pos.x.toUInt()
			var y = pos.y.toUInt()
			var z = pos.z.toUInt()

			x = (x xor (y * 0x27d4eb2du))
			y = (y xor (z * 0x165667b1u))
			z = (z xor (x * 0x1b873593u))

			x = (x xor (x shr 16)) * 0x85ebca6bu
			y = (y xor (y shr 13)) * 0xc2b2ae35u
			z = (z xor (z shr 16)) * 0x27d4eb2du

			val scale = 1.0f / (1L shl 32).toFloat()
			val xf = (x.toULong() and 0xFFFFFFFFu).toFloat() * scale
			val yf = (y.toULong() and 0xFFFFFFFFu).toFloat() * scale
			val zf = (z.toULong() and 0xFFFFFFFFu).toFloat() * scale

			return Vector3f(xf, yf, zf)
		}
	}
}