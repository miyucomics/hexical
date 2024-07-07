package miyucomics.hexical.entities

import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class SpikeRenderer(ctx: EntityRendererFactory.Context?) : EntityRenderer<SpikeEntity?>(ctx) {
	override fun getTexture(spike: SpikeEntity?) = Identifier("textures/block/amethyst_cluster.png")
	override fun render(spike: SpikeEntity?, yaw: Float, deltaTick: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
		matrices.push()
		matrices.pop()
	}
}