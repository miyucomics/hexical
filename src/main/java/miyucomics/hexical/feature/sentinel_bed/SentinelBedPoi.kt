package miyucomics.hexical.feature.sentinel_bed

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.init.HexicalBlocks
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.`object`.builder.v1.world.poi.PointOfInterestHelper
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

object SentinelBedPoi : InitHook() {
	private val SENTINEL_BED_POI_KEY = RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, HexicalMain.id("sentinel_bed"))

	override fun init() {
		PointOfInterestHelper.register(HexicalMain.id("sentinel_bed"), 0, 1, HexicalBlocks.SENTINEL_BED_BLOCK.stateManager.states.toSet())
	}

	fun isSentinelBed(world: ServerWorld, centerPos: BlockPos): Boolean {
		return world.pointOfInterestStorage.getType(centerPos).filter { it.matchesKey(SENTINEL_BED_POI_KEY) }.isPresent
	}
}