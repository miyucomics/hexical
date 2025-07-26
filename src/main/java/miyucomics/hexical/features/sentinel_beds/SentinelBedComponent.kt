package miyucomics.hexical.features.sentinel_beds

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.CastingEnvironmentComponent
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class SentinelBedComponent(val env: CastingEnvironment) : CastingEnvironmentComponent.IsVecInRange {
	override fun getKey() = SentinelBedKey()
	class SentinelBedKey : CastingEnvironmentComponent.Key<CastingEnvironmentComponent.IsVecInRange>

	override fun onIsVecInRange(vec: Vec3d, current: Boolean): Boolean {
		return current || SentinelBedPoi.isSentinelBed(env.world, BlockPos.ofFloored(vec))
	}
}