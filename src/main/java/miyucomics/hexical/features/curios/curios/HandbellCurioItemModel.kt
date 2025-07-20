package miyucomics.hexical.features.curios.curios

import dev.kosmx.playerAnim.api.layered.ModifierLayer
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.inits.InitHook
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.util.ModelIdentifier

object HandbellCurioItemModel : InitHook() {
	@JvmField val heldHandbellModel: ModelIdentifier = ModelIdentifier("hexical", "held_curio_handbell", "inventory")
	@JvmField val handbellModel: ModelIdentifier = ModelIdentifier("hexical", "curio_handbell", "inventory")
	val clientReceiver = HexicalMain.id("handbell")

	override fun init() {
		ClientPlayNetworking.registerGlobalReceiver(clientReceiver) { client, handler, buf, responseSender ->
			val player = client.world!!.getPlayerByUuid(buf.readUuid()) ?: return@registerGlobalReceiver
			val handbellAnimation = (PlayerAnimationAccess.getPlayerAssociatedData(player as AbstractClientPlayerEntity).get(clientReceiver) as ModifierLayer<HandbellCurioPlayerModel>).animation
			handbellAnimation!!.shakingBellTimer = 10
		}
	}
}