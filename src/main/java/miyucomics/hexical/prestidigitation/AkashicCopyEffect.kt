package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos

class AkashicCopyEffect : PrestidigitationEffect {
	override fun effectBlock(env: CastingEnvironment, position: BlockPos) {
		if (env.castingEntity !is ServerPlayerEntity)
			return
		val caster = env.castingEntity as ServerPlayerEntity
		val shelf = env.world.getBlockEntity(position)
		if (shelf is BlockEntityAkashicBookshelf) {
			val nbt = shelf.iotaTag ?: return
			val vm = IXplatAbstractions.INSTANCE.getStaffcastVM(caster, Hand.MAIN_HAND)
			vm.image.stack.toMutableList().add(IotaType.deserialize(nbt, caster.serverWorld))
			IXplatAbstractions.INSTANCE.setStaffcastImage(caster, vm.image)
		}
	}

	override fun effectEntity(env: CastingEnvironment, entity: Entity) {}
}