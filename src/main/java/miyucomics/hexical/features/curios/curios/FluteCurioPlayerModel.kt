package miyucomics.hexical.features.curios.curios

import dev.kosmx.playerAnim.api.TransformType
import dev.kosmx.playerAnim.api.layered.IAnimation
import dev.kosmx.playerAnim.core.util.Vec3f
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.misc.ClientStorage
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.math.MathHelper

class FluteCurioPlayerModel(val player: PlayerEntity) : IAnimation {
	override fun setupAnim(tickDelta: Float) {}
	override fun isActive() = player.getStackInHand(Hand.MAIN_HAND).isOf(HexicalItems.CURIO_FLUTE) || player.getStackInHand(Hand.OFF_HAND).isOf(HexicalItems.CURIO_FLUTE)
	override fun get3DTransform(modelName: String, type: TransformType, tickDelta: Float, original: Vec3f): Vec3f {
		val time = ClientStorage.ticks + tickDelta
		val pitch = MathHelper.sin(time * 0.08f + 0.8f)
		val yaw = MathHelper.sin(time * 0.06f + 0.2f)
		val roll = MathHelper.sin(time * 0.07f)
		val offset = Vec3f(pitch, yaw, roll)

		if (modelName == "rightArm" && type == TransformType.ROTATION)
			return Vec3f(320f, 0f, 100f).add(offset).scale(MathHelper.RADIANS_PER_DEGREE)
		if (modelName == "leftArm" && type == TransformType.ROTATION)
			return Vec3f(285f, 60f, 10f).add(offset).scale(MathHelper.RADIANS_PER_DEGREE)
		if (modelName == "rightItem" && type == TransformType.POSITION)
			return Vec3f(2f, 5f, 8f)
		if (modelName == "rightItem" && type == TransformType.ROTATION)
			return Vec3f(60f, 0f, -15f).scale(MathHelper.RADIANS_PER_DEGREE)
		return original
	}
}