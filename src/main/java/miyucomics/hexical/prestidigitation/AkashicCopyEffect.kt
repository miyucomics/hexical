package miyucomics.hexical.prestidigitation

import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
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
			val harness = IXplatAbstractions.INSTANCE.getHarness(caster, Hand.MAIN_HAND)
			harness.stack.add(HexIotaTypes.deserialize(nbt, caster.world as ServerWorld))
			IXplatAbstractions.INSTANCE.setHarness(caster, harness)
		}
	}

	override fun effectEntity(caster: ServerPlayerEntity, entity: Entity) {}
}