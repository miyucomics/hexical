package miyucomics.hexical.features.player

import dev.kosmx.playerAnim.api.layered.ModifierLayer
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess
import miyucomics.hexical.features.curios.curios.FluteCurioPlayerModel
import miyucomics.hexical.features.curios.curios.HandbellCurioItemModel
import miyucomics.hexical.features.curios.curios.HandbellCurioPlayerModel
import miyucomics.hexical.features.dance.DanceAnimation
import miyucomics.hexical.features.evocation.EvocationAnimation
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.misc.InitHook
import net.minecraft.util.Arm
import net.minecraft.util.Hand

object PlayerAnimatorHook : InitHook() {
	override fun init() {
		PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register { player, stack ->
			stack.addAnimLayer(300, DanceAnimation(player))

			stack.addAnimLayer(200, EvocationAnimation(player))

			stack.addAnimLayer(100, ModifierLayer(FluteCurioPlayerModel(player)).also {
				it.addModifierBefore(object : MirrorModifier() {
					override fun isEnabled() = (player.mainArm == Arm.LEFT) xor (player.getStackInHand(Hand.OFF_HAND).isOf(HexicalItems.CURIO_FLUTE) && !player.getStackInHand(Hand.MAIN_HAND).isOf(HexicalItems.CURIO_FLUTE))
				})
			})

			val handbellAnimation = ModifierLayer(HandbellCurioPlayerModel(player)).also {
				it.addModifierBefore(object : MirrorModifier() {
					override fun isEnabled() = (player.mainArm == Arm.LEFT) xor (player.getStackInHand(Hand.OFF_HAND).isOf(HexicalItems.CURIO_HANDBELL) && !player.getStackInHand(Hand.MAIN_HAND).isOf(HexicalItems.CURIO_HANDBELL))
				})
			}
			PlayerAnimationAccess.getPlayerAssociatedData(player).set(HandbellCurioItemModel.clientReceiver, handbellAnimation)
			stack.addAnimLayer(100, handbellAnimation)
		}
	}
}