package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.math.BlockPos

class AkashicCopyEffect(private val property: BooleanProperty) : PrestidigitationEffect {
	override fun getCost() = MediaConstants.DUST_UNIT / 100

	override fun effectBlock(caster: ServerPlayerEntity, position: BlockPos) {
		val state = caster.world.getBlockState(position)
		caster.world.setBlockState(position, state.with(property, !state.get(property)), Block.NOTIFY_ALL or Block.REDRAW_ON_MAIN_THREAD)
	}

	override fun effectEntity(caster: ServerPlayerEntity, entity: Entity) {}
}