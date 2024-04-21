package miyucomics.hexical.entities

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.EntityModelPartNames
import net.minecraft.client.render.entity.model.ModelWithHat
import net.minecraft.client.render.entity.model.ModelWithHead
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import net.minecraft.entity.Entity
import net.minecraft.entity.passive.MerchantEntity
import net.minecraft.util.math.MathHelper

@Environment(value = EnvType.CLIENT)
class StatueEntityModel(private val root: ModelPart) : SinglePartEntityModel<StatueEntity>(), ModelWithHead {
	private val head: ModelPart = root.getChild(EntityModelPartNames.HEAD)
	private val rightLeg: ModelPart = root.getChild(EntityModelPartNames.RIGHT_LEG)
	private val leftLeg: ModelPart = root.getChild(EntityModelPartNames.LEFT_LEG)
	private val nose: ModelPart = head.getChild(EntityModelPartNames.NOSE)

	override fun getPart(): ModelPart {
		return this.root
	}

	override fun setAngles(entity: StatueEntity, limbAngle: Float, limbDistance: Float, animationProgress: Float, headYaw: Float, headPitch: Float) {
		head.yaw = headYaw * (Math.PI.toFloat() / 180)
		head.pitch = headPitch * (Math.PI.toFloat() / 180)

		// when adding possession, change these
		rightLeg.pitch = 0f
		leftLeg.pitch = 0f

		rightLeg.yaw = 0.0f
		leftLeg.yaw = 0.0f
	}

	override fun getHead(): ModelPart {
		return this.head
	}

	companion object {
		fun getModelData(): ModelData {
			val modelData = ModelData()
			val modelPartData = modelData.root
			val modelPartData2 = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0f, -10.0f, -4.0f, 8.0f, 10.0f, 8.0f), ModelTransform.NONE)
			modelPartData2.addChild(EntityModelPartNames.NOSE, ModelPartBuilder.create().uv(24, 0).cuboid(-1.0f, -1.0f, -6.0f, 2.0f, 4.0f, 2.0f), ModelTransform.pivot(0.0f, -2.0f, 0.0f))
			val modelPartData4 = modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(16, 20).cuboid(-4.0f, 0.0f, -3.0f, 8.0f, 12.0f, 6.0f), ModelTransform.NONE)
			modelPartData4.addChild(EntityModelPartNames.JACKET, ModelPartBuilder.create().uv(0, 38).cuboid(-4.0f, 0.0f, -3.0f, 8.0f, 20.0f, 6.0f, Dilation(0.5f)), ModelTransform.NONE)
			modelPartData.addChild(EntityModelPartNames.ARMS, ModelPartBuilder.create().uv(44, 22).cuboid(-8.0f, -2.0f, -2.0f, 4.0f, 8.0f, 4.0f).uv(44, 22).cuboid(4.0f, -2.0f, -2.0f, 4.0f, 8.0f, 4.0f, true).uv(40, 38).cuboid(-4.0f, 2.0f, -2.0f, 8.0f, 4.0f, 4.0f), ModelTransform.of(0.0f, 3.0f, -1.0f, -0.75f, 0.0f, 0.0f))
			modelPartData.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(0, 22).cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f), ModelTransform.pivot(-2.0f, 12.0f, 0.0f))
			modelPartData.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create().uv(0, 22).mirrored().cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f), ModelTransform.pivot(2.0f, 12.0f, 0.0f))
			return modelData
		}
	}
}