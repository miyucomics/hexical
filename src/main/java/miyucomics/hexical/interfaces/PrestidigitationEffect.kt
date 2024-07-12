package miyucomics.hexical.interfaces

import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

interface PrestidigitationEffect {
	fun effectBlock(caster: ServerPlayerEntity, position: BlockPos)
	fun effectEntity(caster: ServerPlayerEntity, entity: Entity)
}