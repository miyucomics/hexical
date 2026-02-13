package miyucomics.hexical.features.toast

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

object ToastReceiver : InitHook() {
	override fun init() {
		ClientPlayNetworking.registerGlobalReceiver(HexicalMain.id("toast")) { client, _, buf, _ ->
			val title = buf.readText()
			val description = buf.readText()
			val stack = buf.readItemStack()
			client.execute {
				client.toastManager.add(HexToast(title, description, stack))
			}
		}
	}
}