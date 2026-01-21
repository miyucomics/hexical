package miyucomics.hexical.features.curios

import dev.kosmx.playerAnim.api.layered.ModifierLayer
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess
import miyucomics.hexical.features.curios.curios.HandbellCurio
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.util.ModelIdentifier

object HandbellCurioItemModel : InitHook() {
	@JvmField val heldHandbellModel: ModelIdentifier = ModelIdentifier("hexical", "held_curio_handbell", "inventory")
	@JvmField val handbellModel: ModelIdentifier = ModelIdentifier("hexical", "curio_handbell", "inventory")

	override fun init() {
		ModelLoadingPlugin.register { context ->
			context.addModels(heldHandbellModel)
		}

		ClientPlayNetworking.registerGlobalReceiver(HandbellCurio.CHANNEL) { client, _, buf, _ ->
			val player = client.world!!.getPlayerByUuid(buf.readUuid()) ?: return@registerGlobalReceiver
			val handbellAnimation = (PlayerAnimationAccess.getPlayerAssociatedData(player as AbstractClientPlayerEntity).get(HandbellCurio.CHANNEL) as ModifierLayer<HandbellCurioPlayerModel>).animation
			handbellAnimation!!.shakingBellTimer = 10
		}
	}
}