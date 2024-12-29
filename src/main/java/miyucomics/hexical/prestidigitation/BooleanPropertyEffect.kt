package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.math.BlockPos

class BooleanPropertyEffect(private val property: BooleanProperty) : PrestidigitationEffect {
	override fun effectBlock(env: CastingEnvironment, position: BlockPos) {
		val state = env.world.getBlockState(position)
		env.world.setBlockState(position, state.with(property, !state.get(property)), Block.NOTIFY_ALL or Block.REDRAW_ON_MAIN_THREAD)
	}

	override fun effectEntity(env: CastingEnvironment, entity: Entity) {}
}