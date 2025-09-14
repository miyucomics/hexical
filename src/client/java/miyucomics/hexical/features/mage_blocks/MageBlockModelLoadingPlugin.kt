package miyucomics.hexical.features.mage_blocks

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.inits.HexicalBlocks
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier.OnLoad
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.util.Identifier

object MageBlockModelLoadingHook : InitHook() {
    override fun init() {
        ColorProviderRegistry.BLOCK.register({ _, world, pos, tintIndex ->
            if (world == null || pos == null)
                return@register -1

            if (world.getBlockEntity(pos) is MageBlockEntity) {
                val disguiseState = (world.getBlockEntity(pos) as MageBlockEntity).disguise
                return@register MinecraftClient.getInstance().blockColors.getColor(disguiseState, world, pos, tintIndex)
            }

            -1
        }, HexicalBlocks.MAGE_BLOCK)

        ModelLoadingPlugin.register { pluginContext ->
            pluginContext.modifyModelOnLoad().register { original: UnbakedModel, context: OnLoad.Context ->
                when (context.id()) {
                    ModelIdentifier(HexicalMain.id("mage_block"), "") -> MageBlockModel()
                    ModelIdentifier(HexicalMain.id("mage_block"), "inventory") -> context.getOrLoadModel(Identifier("item/amethyst_shard"))
                    else -> original
                }
            }
        }
    }
}