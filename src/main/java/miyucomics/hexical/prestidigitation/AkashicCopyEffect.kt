package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import miyucomics.hexical.interfaces.PrestidigitationEffect
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

class AkashicCopyEffect : PrestidigitationEffect {
	override fun effectBlock(env: CastingEnvironment, position: BlockPos) {
		if (env.castingEntity !is ServerPlayerEntity)
			return
		val caster = env.castingEntity as ServerPlayerEntity
		val shelf = env.world.getBlockEntity(position)
		if (shelf is BlockEntityAkashicBookshelf) {
			val nbt = shelf.iotaTag ?: return
			CastingUtils.giveIota(caster, IotaType.deserialize(nbt, caster.serverWorld))
		}
	}

	override fun effectEntity(env: CastingEnvironment, entity: Entity) {}
}