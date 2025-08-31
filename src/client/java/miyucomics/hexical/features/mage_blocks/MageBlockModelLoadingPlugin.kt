package miyucomics.hexical.features.mage_blocks

import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier.OnLoad
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.util.ModelIdentifier

object MageBlockModelLoadingHook : InitHook() {
    override fun init() {
        ModelLoadingPlugin.register { pluginContext: ModelLoadingPlugin.Context ->
            pluginContext.modifyModelOnLoad().register { original: UnbakedModel, context: OnLoad.Context ->
                val id = context.id() as? ModelIdentifier
                if (id != null && id.namespace == "hexical" && id.path == "mage_block")
                    return@register MageBlockModel()
                return@register original
            }
        }
    }
}