package miyucomics.hexical.features.pedestal

import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random

class PedestalBlockEntityRenderer(context: BlockEntityRendererFactory.Context) : BlockEntityRenderer<PedestalBlockEntity> {
	private val itemRenderer = context.itemRenderer
	private val random = Random.create()

	override fun render(pedestal: PedestalBlockEntity, tickDelta: Float, matrices: MatrixStack, vertices: VertexConsumerProvider, light: Int, overlay: Int) {
		if (pedestal.heldStack.isEmpty)
			return

		val time = pedestal.world!!.time + tickDelta
		val offset = Vec3d.of(pedestal.normalVector).multiply(PedestalBlockEntity.HEIGHT)
		val light = WorldRenderer.getLightmapCoordinates(pedestal.world, pedestal.pos.add(pedestal.normalVector))
		random.setSeed((if (pedestal.heldStack.isEmpty) 187 else Item.getRawId(pedestal.heldStack.item) + pedestal.heldStack.damage).toLong())
		val bakedModel = itemRenderer.getModel(pedestal.heldStack, pedestal.world, null, 0)
		val hasDepth = bakedModel.hasDepth()
		val renderedAmount = getRenderedAmount(pedestal.heldStack)
		val scaleX = bakedModel.transformation.ground.scale.x()
		val scaleY = bakedModel.transformation.ground.scale.y()
		val scaleZ = bakedModel.transformation.ground.scale.z()

		matrices.push()
		matrices.translate(offset.x + 0.5, offset.y + 0.35, offset.z + 0.5)
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 4))

		if (!hasDepth) {
			val initialOffsetZ = -Z_LAYER_OFFSET * (renderedAmount - 1) * scaleZ / 2f
			matrices.translate(0f, 0f, initialOffsetZ)
		}

		for (i in 0 until renderedAmount) {
			matrices.push()

			if (i > 0) {
				if (hasDepth) {
					val randomX = (random.nextFloat() * 2.0f - 1.0f) * ITEM_OFFSET_MULTIPLIER
					val randomY = (random.nextFloat() * 2.0f - 1.0f) * ITEM_OFFSET_MULTIPLIER
					val randomZ = (random.nextFloat() * 2.0f - 1.0f) * ITEM_OFFSET_MULTIPLIER
					matrices.translate(randomX, randomY, randomZ)
				} else {
					val randomX = (random.nextFloat() * 2.0f - 1.0f) * ITEM_OFFSET_MULTIPLIER * 0.5f
					val randomY = (random.nextFloat() * 2.0f - 1.0f) * ITEM_OFFSET_MULTIPLIER * 0.5f
					matrices.translate(randomX, randomY, 0.0f)
				}
			}

			itemRenderer.renderItem(pedestal.heldStack, ModelTransformationMode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertices, pedestal.world, 0)
			matrices.pop()
			if (!hasDepth)
				matrices.translate(0.0f * scaleX, 0.0f * scaleY, Z_LAYER_OFFSET * scaleZ)
		}

		matrices.pop()
	}

	private fun getRenderedAmount(stack: ItemStack): Int {
		return when {
			stack.count > 48 -> 5
			stack.count > 32 -> 4
			stack.count > 16 -> 3
			stack.count > 1 -> 2
			else -> 1
		}
	}

	companion object {
		private const val ITEM_OFFSET_MULTIPLIER = 0.15f
		private const val Z_LAYER_OFFSET = 0.09375f
	}
}