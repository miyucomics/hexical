package miyucomics.hexical.interfaces

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos

interface PrestidigitationEffect {
	fun effectBlock(env: CastingEnvironment, position: BlockPos)
	fun effectEntity(env: CastingEnvironment, entity: Entity)
}