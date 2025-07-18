package miyucomics.hexical.features.charms

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import jdk.incubator.vector.VectorShuffle.iota
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.curios.CurioItem
import miyucomics.hexical.inits.InitHook
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.util.Hand
import net.minecraft.util.Identifier

object ServerCharmedUseReceiver : InitHook() {
	@JvmField
	val CHARMED_ITEM_USE_CHANNEL: Identifier = HexicalMain.id("charmed_item")

	override fun init() {
		ServerPlayNetworking.registerGlobalReceiver(CHARMED_ITEM_USE_CHANNEL) { server, player, _, buf, _ ->
			val inputMethod = buf.readInt()
			val hand = enumValues<Hand>()[buf.readInt()]
			val stack = player.getStackInHand(hand)
			server.execute {
				val vm = CastingVM(CastingImage().copy(stack = inputMethod.asActionResult), CharmCastEnv(player, hand, stack))
				vm.queueExecuteAndWrapIotas(CharmUtilities.getHex(stack, player.serverWorld), player.serverWorld)
				if (stack.item is CurioItem)
					(stack.item as CurioItem).postUse(player, stack, player.serverWorld)
			}
		}
	}
}