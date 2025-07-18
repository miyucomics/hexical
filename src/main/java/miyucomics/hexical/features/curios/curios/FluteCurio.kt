package miyucomics.hexical.features.curios.curios

import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.common.msgs.MsgBeepS2C
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.features.charms.CharmUtilities
import miyucomics.hexical.features.curios.CurioItem
import net.minecraft.block.enums.Instrument
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.event.GameEvent
import kotlin.math.abs
import kotlin.math.roundToInt

object FluteCurio : CurioItem() {
	override fun postUse(user: ServerPlayerEntity, stack: ItemStack, world: ServerWorld) {
		val note = CharmUtilities.getInternalStorage(stack, world)
		if (note !is DoubleIota || note.double < 0 || note.double > 24 || abs(note.double - note.double.roundToInt()) > DoubleIota.TOLERANCE)
			return
		val position = user.eyePos.add(user.rotationVector)
		IXplatAbstractions.INSTANCE.sendPacketNear(position, 128.0, world, MsgBeepS2C(position, note.double.toInt(), Instrument.FLUTE))
		world.emitGameEvent(null, GameEvent.NOTE_BLOCK_PLAY, position)
	}
}