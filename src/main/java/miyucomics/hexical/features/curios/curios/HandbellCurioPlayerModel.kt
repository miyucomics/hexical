package miyucomics.hexical.features.curios.curios

import dev.kosmx.playerAnim.api.TransformType
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode
import dev.kosmx.playerAnim.api.layered.IAnimation
import dev.kosmx.playerAnim.core.util.Vec3f
import miyucomics.hexical.inits.HexicalItems
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.math.MathHelper

class HandbellCurioPlayerModel(val player: PlayerEntity) : IAnimation {
	override fun setupAnim(tickDelta: Float) {}
	override fun isActive() = player.getStackInHand(Hand.MAIN_HAND).isOf(HexicalItems.CURIO_HANDBELL) || player.getStackInHand(Hand.OFF_HAND).isOf(HexicalItems.CURIO_HANDBELL)
	override fun get3DTransform(modelName: String, type: TransformType, tickDelta: Float, original: Vec3f): Vec3f {
		if (modelName == "rightArm" && type == TransformType.ROTATION)
			return Vec3f(-95f, 15f, 0f).scale(MathHelper.RADIANS_PER_DEGREE)
		return original
	}
}