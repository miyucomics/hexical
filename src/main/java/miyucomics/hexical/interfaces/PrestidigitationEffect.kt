package miyucomics.hexical.interfaces

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.BlockPos

interface PrestidigitationEffect {
	fun effectBlock(caster: LivingEntity?, position: BlockPos)
	fun effectEntity(caster: LivingEntity?, entity: Entity)
}