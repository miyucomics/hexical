package miyucomics.hexical.prestidigitation

import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.PistonBlockEntity
import net.minecraft.block.piston.PistonHandler
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.property.Property
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

class PistonEffect : PrestidigitationEffect {
	override fun effectBlock(caster: ServerPlayerEntity, position: BlockPos) {
		val state = caster.world.getBlockState(position)
	}

	override fun effectEntity(caster: ServerPlayerEntity, entity: Entity) {}
}