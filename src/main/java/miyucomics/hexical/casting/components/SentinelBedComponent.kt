package miyucomics.hexical.casting.components

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.CastingEnvironmentComponent
import at.petrak.hexcasting.api.casting.eval.CastingEnvironmentComponent.IsVecInRange
import miyucomics.hexical.registry.HexicalBlocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class SentinelBedComponent(val env: CastingEnvironment) : IsVecInRange {
	override fun getKey() = SentinelBedKey()
	class SentinelBedKey : CastingEnvironmentComponent.Key<IsVecInRange>

	override fun onIsVecInRange(vec: Vec3d?, current: Boolean): Boolean {
		return current || env.world.getBlockState(BlockPos.ofFloored(vec)).isOf(HexicalBlocks.SENTINEL_BED_BLOCK)
	}
}