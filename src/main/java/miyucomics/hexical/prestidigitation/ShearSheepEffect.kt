package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.entity.Entity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.util.math.BlockPos

class ShearSheepEffect : PrestidigitationEffect {
	override fun getCost() = MediaConstants.DUST_UNIT / 5
	override fun effectBlock(caster: ServerPlayerEntity, position: BlockPos) {}
	override fun effectEntity(caster: ServerPlayerEntity, entity: Entity) {
		if (entity is SheepEntity)
			entity.sheared(SoundCategory.PLAYERS)
	}
}