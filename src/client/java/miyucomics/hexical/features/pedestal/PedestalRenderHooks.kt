package miyucomics.hexical.features.pedestal

import miyucomics.hexical.inits.HexicalBlocks.PEDESTAL_BLOCK_ENTITY
import miyucomics.hexical.misc.InitHook
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories

object PedestalRenderHooks : InitHook() {
	override fun init() {
		BlockEntityRendererFactories.register(PEDESTAL_BLOCK_ENTITY, ::PedestalBlockEntityRenderer)
	}
}