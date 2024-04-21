package miyucomics.hexical.entities

import miyucomics.hexical.Hexical
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer
import net.minecraft.client.render.entity.feature.VillagerHeldItemFeatureRenderer
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class StatueRenderer(context: EntityRendererFactory.Context?) : LivingEntityRenderer<StatueEntity?, StatueEntityModel>(context, StatueEntityModel(context!!.getPart(EntityModelLayers.VILLAGER)), 0.5f) {
	init {
		this.addFeature(HeadFeatureRenderer<StatueEntity, StatueEntityModel>(this, context?.modelLoader, context?.heldItemRenderer))
		this.addFeature(VillagerHeldItemFeatureRenderer<StatueEntity, StatueEntityModel>(this, context?.heldItemRenderer))
	}

	override fun scale(entity: StatueEntity?, matrixStack: MatrixStack?, f: Float) {
		val g = 0.9375f
		this.shadowRadius = 0.5f
		matrixStack?.scale(g, g, g)
	}

	override fun getTexture(entity: StatueEntity?) = TEXTURE
	override fun hasLabel(livingEntity: StatueEntity?) = false
	companion object {
		private val TEXTURE = Identifier(Hexical.MOD_ID, "textures/entity/statue.png")
	}
}