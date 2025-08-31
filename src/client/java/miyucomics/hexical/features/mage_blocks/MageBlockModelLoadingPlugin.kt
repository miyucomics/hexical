package miyucomics.hexical.features.mage_blocks

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier.OnLoad
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.util.Identifier

object MageBlockModelLoadingHook : InitHook() {
    override fun init() {
        ModelLoadingPlugin.register { pluginContext ->
            pluginContext.modifyModelOnLoad().register { original: UnbakedModel, context: OnLoad.Context ->
                when (context.id() as? ModelIdentifier) {
                    ModelIdentifier(HexicalMain.id("mage_block"), "") -> MageBlockModel()
                    ModelIdentifier(HexicalMain.id("mage_block"), "inventory") -> context.getOrLoadModel(Identifier("item/amethyst_shard"))
                    else -> original
                }
            }
        }
    }
}