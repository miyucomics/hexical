package miyucomics.hexical.data.prestidigitation

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos

interface PrestidigitationHandler {
	fun tryHandleBlock(env: CastingEnvironment, position: BlockPos): Boolean = false
	fun tryHandleEntity(env: CastingEnvironment, entity: Entity): Boolean = false
}