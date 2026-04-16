package miyucomics.hexical.feature.player_animation

import dev.kosmx.playerAnim.api.layered.ModifierLayer
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess
import miyucomics.hexical.feature.curio.FluteCurioPlayerModel
import miyucomics.hexical.feature.curio.HandbellCurioPlayerModel
import miyucomics.hexical.feature.curio.curios.HandbellCurio
import miyucomics.hexical.feature.evocation.EvocationAnimation
import miyucomics.hexical.init.HexicalItems
import miyucomics.hexical.misc.InitHook
import net.minecraft.util.Arm
import net.minecraft.util.Hand

object PlayerAnimatorHook : InitHook() {
	override fun init() {
		PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register { player, stack ->
			stack.addAnimLayer(200, EvocationAnimation(player))

			stack.addAnimLayer(100, ModifierLayer(FluteCurioPlayerModel(player)).also {
				it.addModifierBefore(object : MirrorModifier() {
					override fun isEnabled() = (player.mainArm == Arm.LEFT) xor (player.getStackInHand(Hand.OFF_HAND).isOf(HexicalItems.CURIO_FLUTE) && !player.getStackInHand(Hand.MAIN_HAND).isOf(HexicalItems.CURIO_FLUTE))
				})
			})

			stack.addAnimLayer(100, ModifierLayer(HandbellCurioPlayerModel(player)).also {
				it.addModifierBefore(object : MirrorModifier() {
					override fun isEnabled() = (player.mainArm == Arm.LEFT) xor (player.getStackInHand(Hand.OFF_HAND).isOf(HexicalItems.CURIO_HANDBELL) && !player.getStackInHand(Hand.MAIN_HAND).isOf(HexicalItems.CURIO_HANDBELL))
				})
				PlayerAnimationAccess.getPlayerAssociatedData(player).set(HandbellCurio.CHANNEL, it)
			})
		}
	}
}