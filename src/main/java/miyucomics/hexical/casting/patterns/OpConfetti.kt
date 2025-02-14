package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveDoubleUnderInclusive
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.inits.HexicalNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.util.math.Vec3d

class OpConfetti : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getVec3(0, argc)
		env.assertVecInRange(position)
		val velocity = args.getVec3(1, argc).normalize()
		val speed = args.getPositiveDoubleUnderInclusive(2, 2.0, argc) / 2
		return SpellAction.Result(Spell(position, velocity, speed), MediaConstants.DUST_UNIT / 2, listOf())
	}

	private data class Spell(val pos: Vec3d, val dir: Vec3d, val speed: Double) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val packet = PacketByteBufs.create()
			packet.writeDouble(pos.x)
			packet.writeDouble(pos.y)
			packet.writeDouble(pos.z)
			packet.writeDouble(dir.x)
			packet.writeDouble(dir.y)
			packet.writeDouble(dir.z)
			packet.writeDouble(speed)
			env.world.players.forEach { player -> env.world.sendToPlayerIfNearby(player, false, pos.x, pos.y, pos.z, ServerPlayNetworking.createS2CPacket(HexicalNetworking.CONFETTI_CHANNEL, packet)) }
		}
	}
}