package miyucomics.hexical.features.patchouli

import miyucomics.hexical.HexicalMain
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderLayers
import net.minecraft.util.math.RotationAxis

object PageUtils {
	fun renderBlock(graphics: DrawContext, state: BlockState, centerX: Int, centerY: Int, scale: Float) {
		val matrices = graphics.matrices
		matrices.push()
		matrices.translate(centerX.toFloat(), centerY.toFloat(), 100f)
		matrices.scale(scale, -scale, scale)
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(30f))
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45f))
		matrices.translate(-0.5, -0.5, -0.5)

		val client = MinecraftClient.getInstance()
		val manager = client.blockRenderManager
		val consumers = client.bufferBuilders.entityVertexConsumers
		manager.renderBlock(state, client.player!!.blockPos.add(0, 1000, 0), client.world, matrices, consumers.getBuffer(RenderLayers.getBlockLayer(state)), false, HexicalMain.RANDOM)
		consumers.draw()

		matrices.pop()
	}
}