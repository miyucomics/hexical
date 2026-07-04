package miyucomics.hexical.features.babelbug

import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import miyucomics.hexical.features.item_cache.itemCache
import miyucomics.hexical.misc.InitHook
import miyucomics.hexpose.iotas.asActionResult
import net.fabricmc.fabric.api.message.v1.ServerMessageDecoratorEvent
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Hand
import java.util.concurrent.CompletableFuture

object BabelbugListener : InitHook() {
	override fun init() {
		ServerMessageDecoratorEvent.EVENT.register(ServerMessageDecoratorEvent.CONTENT_PHASE) { sender: ServerPlayerEntity?, message: Text ->
			if (sender == null)
				return@register CompletableFuture.completedFuture(message)
			val babelbugProgram = sender.itemCache().babelbugProgram ?: return@register CompletableFuture.completedFuture(message)

			val contents = message.string
			val vm = CastingVM(CastingImage().copy(stack = Text.literal(contents).asActionResult), BabelbugCastEnv(sender, Hand.MAIN_HAND))
			vm.queueExecuteAndWrapIotas(babelbugProgram, sender.serverWorld)

			val remainingStack = vm.image.stack
			if (remainingStack.isNotEmpty())
				return@register CompletableFuture.completedFuture(remainingStack[remainingStack.lastIndex].display())

			// this technically errors but it does our job of cancelling the message so... all is well that ends well?
			null
		}
	}
}