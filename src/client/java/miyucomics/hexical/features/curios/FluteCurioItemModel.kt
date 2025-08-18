package miyucomics.hexical.features.curios

import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.minecraft.client.util.ModelIdentifier

object FluteCurioItemModel : InitHook() {
	@JvmField val heldFluteModel: ModelIdentifier = ModelIdentifier("hexical", "held_curio_flute", "inventory")
	@JvmField val fluteModel: ModelIdentifier = ModelIdentifier("hexical", "curio_flute", "inventory")

	override fun init() {
		ModelLoadingPlugin.register { context ->
			context.addModels(heldFluteModel)
		}
	}
}