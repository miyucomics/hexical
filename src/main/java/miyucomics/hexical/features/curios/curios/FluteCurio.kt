package miyucomics.hexical.features.curios.curios

import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.common.msgs.MsgBeepS2C
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.features.curios.CurioItem
import net.minecraft.block.enums.Instrument
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt

object FluteCurio : CurioItem() {
	override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
		if (world.isClient)
			return TypedActionResult.pass(user.getStackInHand(hand))
		playSound(world, user, hand, floor((MathHelper.clamp(user.pitch, -40f, 40f) / -80f + 0.5f) * 25f).toInt())
		return TypedActionResult.pass(user.getStackInHand(hand))
	}

	override fun postUse(user: ServerPlayerEntity, item: ItemStack, hand: Hand, world: ServerWorld, stack: List<Iota>) {
		val note = stack.lastOrNull()
		if (note !is DoubleIota || note.double < 0 || note.double > 24 || abs(note.double - note.double.roundToInt()) > DoubleIota.TOLERANCE)
			return
		playSound(world, user, hand, note.double.toInt())
	}

	private fun playSound(world: World, user: PlayerEntity, hand: Hand, note: Int) {
		val upPitch = (-user.pitch + 90) * (Math.PI.toFloat() / 180)
		val yaw = -user.headYaw * (Math.PI.toFloat() / 180)
		val j = MathHelper.cos(upPitch).toDouble()
		val upAxis = Vec3d(MathHelper.sin(yaw).toDouble() * j, MathHelper.sin(upPitch).toDouble(), MathHelper.cos(yaw).toDouble() * j)

		val position = user.eyePos.add(user.rotationVector.crossProduct(upAxis).multiply(if (hand == Hand.MAIN_HAND) 0.6 else -0.6))
		IXplatAbstractions.INSTANCE.sendPacketNear(position, 128.0, world as ServerWorld, MsgBeepS2C(position, note, Instrument.FLUTE))
		world.emitGameEvent(null, GameEvent.NOTE_BLOCK_PLAY, position)
	}
}