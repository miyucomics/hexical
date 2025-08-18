package miyucomics.hexical.features.mage_blocks

import at.petrak.hexcasting.api.client.ScryingLensOverlayRegistry
import miyucomics.hexical.inits.HexicalBlocks
import miyucomics.hexical.misc.InitHook

object MageBlockScryingOverlay : InitHook() {
	override fun init() {
		ScryingLensOverlayRegistry.addDisplayer(HexicalBlocks.MAGE_BLOCK) { lines, _, pos, _, world, _ -> (world.getBlockEntity(pos) as MageBlockEntity).addScryingLensLines(lines) }
	}
}