package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.interfaces.PrestidigitationEffect
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos

class AkashicCopyEffect : PrestidigitationEffect {
	override fun effectBlock(caster: ServerPlayerEntity, position: BlockPos) {
		val shelf = caster.world.getBlockEntity(position)
		if (shelf is BlockEntityAkashicBookshelf) {
			val nbt = shelf.iotaTag ?: return
			val vm = IXplatAbstractions.INSTANCE.getStaffcastVM(caster, Hand.MAIN_HAND)
			vm.image.stack.toMutableList().add(IotaType.deserialize(nbt, caster.world as ServerWorld))
			IXplatAbstractions.INSTANCE.setStaffcastImage(caster, vm.image)
		}
	}

	override fun effectEntity(caster: ServerPlayerEntity, entity: Entity) {}
}