package miyucomics.hexical.features.curios

import dev.kosmx.playerAnim.api.TransformType
import dev.kosmx.playerAnim.api.layered.IAnimation
import dev.kosmx.playerAnim.core.util.Vec3f
import miyucomics.hexical.ClientStorage
import miyucomics.hexical.inits.HexicalItems
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.math.MathHelper

class HandbellCurioPlayerModel(val player: PlayerEntity) : IAnimation {
	var shakingBellTimer = 0

	override fun setupAnim(tickDelta: Float) {}
	override fun isActive() = player.getStackInHand(Hand.MAIN_HAND).isOf(HexicalItems.CURIO_HANDBELL) || player.getStackInHand(Hand.OFF_HAND).isOf(HexicalItems.CURIO_HANDBELL)

	override fun tick() {
		if (shakingBellTimer > 0)
			shakingBellTimer -= 1
	}

	fun getRoll(tickDelta: Float): Float {
		if (shakingBellTimer == 0)
			return 0f
		val progress = ((1f - shakingBellTimer + tickDelta) / 10f)
		val eased = (1f - MathHelper.cos(progress * MathHelper.PI)) / 2f
		return MathHelper.sin(eased * MathHelper.PI * 2) * 20f
	}

	override fun get3DTransform(modelName: String, type: TransformType, tickDelta: Float, original: Vec3f): Vec3f {
		if (modelName == "rightArm" && type == TransformType.ROTATION) {
			val time = ClientStorage.ticks + tickDelta
			val pitch = MathHelper.sin(time * 0.08f + 0.8f) * 2f
			val yaw = MathHelper.sin(time * 0.06f + 0.2f) * 2f
			val roll = MathHelper.sin(time * 0.07f) * 2f + getRoll(tickDelta)
			return Vec3f(-90f + pitch, yaw, roll).scale(MathHelper.RADIANS_PER_DEGREE)
		}
		return original
	}
}