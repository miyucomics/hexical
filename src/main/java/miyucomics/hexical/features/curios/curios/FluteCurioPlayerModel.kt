package miyucomics.hexical.features.curios.curios

import dev.kosmx.playerAnim.api.TransformType
import dev.kosmx.playerAnim.api.layered.IAnimation
import dev.kosmx.playerAnim.core.util.Vec3f
import miyucomics.hexical.inits.HexicalItems
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.math.MathHelper

class FluteCurioPlayerModel(val player: PlayerEntity) : IAnimation {
	override fun setupAnim(tickDelta: Float) {}
	override fun isActive() = player.getStackInHand(Hand.MAIN_HAND).isOf(HexicalItems.CURIO_FLUTE) || player.getStackInHand(Hand.OFF_HAND).isOf(HexicalItems.CURIO_FLUTE)
	override fun get3DTransform(modelName: String, type: TransformType, tickDelta: Float, original: Vec3f): Vec3f {
		if (modelName == "rightArm" && type == TransformType.ROTATION)
			return Vec3f(320f, 0f, 100f).scale(MathHelper.RADIANS_PER_DEGREE)
		if (modelName == "leftArm" && type == TransformType.ROTATION)
			return Vec3f(285f, 60f, 10f).scale(MathHelper.RADIANS_PER_DEGREE)
		if (modelName == "rightItem" && type == TransformType.POSITION)
			return Vec3f(2f, 5f, 8f)
		if (modelName == "rightItem" && type == TransformType.ROTATION)
			return Vec3f(60f, 0f, -15f).scale(MathHelper.RADIANS_PER_DEGREE)
		return original
	}
}