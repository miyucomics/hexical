package miyucomics.hexical.features.curios.curios

import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.common.msgs.MsgBeepS2C
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.charms.CharmUtilities
import miyucomics.hexical.features.curios.CurioItem
import net.minecraft.block.enums.Instrument
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.command.argument.EntityArgumentType.entity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.event.GameEvent
import kotlin.math.abs
import kotlin.math.roundToInt

object FluteCurio : CurioItem() {
	override fun postUse(user: ServerPlayerEntity, item: ItemStack, hand: Hand, world: ServerWorld, stack: List<Iota>) {
		val note = stack.lastOrNull()
		if (note !is DoubleIota || note.double < 0 || note.double > 24 || abs(note.double - note.double.roundToInt()) > DoubleIota.TOLERANCE)
			return

		val upPitch = (-user.pitch + 90) * (Math.PI.toFloat() / 180)
		val yaw = -user.headYaw * (Math.PI.toFloat() / 180)
		val j = MathHelper.cos(upPitch).toDouble()
		val upAxis = Vec3d(MathHelper.sin(yaw).toDouble() * j, MathHelper.sin(upPitch).toDouble(), MathHelper.cos(yaw).toDouble() * j)

		val position = user.eyePos.add(user.rotationVector.crossProduct(upAxis).multiply(if (hand == Hand.MAIN_HAND) 0.8 else -0.8))
		IXplatAbstractions.INSTANCE.sendPacketNear(position, 128.0, world, MsgBeepS2C(position, note.double.toInt(), Instrument.FLUTE))
		world.emitGameEvent(null, GameEvent.NOTE_BLOCK_PLAY, position)
	}
}