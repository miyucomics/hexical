package miyucomics.hexical.features.charms

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.casting.environments.CharmedItemCastEnv
import miyucomics.hexical.inits.Hook
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.util.Hand
import net.minecraft.util.Identifier

object ServerCharmedUseReceiver : Hook() {
	@JvmField
	val CHARMED_ITEM_USE_CHANNEL: Identifier = HexicalMain.id("charmed_item")

	override fun registerCallbacks() {
		ServerPlayNetworking.registerGlobalReceiver(CHARMED_ITEM_USE_CHANNEL) { server, player, _, buf, _ ->
			val inputMethod = buf.readInt()
			val hand = enumValues<Hand>()[buf.readInt()]
			val stack = player.getStackInHand(hand)
			server.execute {
				val vm = CastingVM(CastingImage().copy(stack = inputMethod.asActionResult), CharmedItemCastEnv(player, hand, stack))
				vm.queueExecuteAndWrapIotas(CharmedItemUtilities.getHex(stack, player.serverWorld), player.serverWorld)
			}
		}
	}
}