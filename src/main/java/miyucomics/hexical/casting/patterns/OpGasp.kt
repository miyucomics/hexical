package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.inits.HexicalSounds
import net.minecraft.entity.Entity
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory

class OpGasp : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val target = args.getEntity(0, argc)
		env.assertEntityInRange(target)
		return SpellAction.Result(Spell(target), MediaConstants.DUST_UNIT, listOf(ParticleSpray.cloud(target.pos, 1.0)))
	}

	private data class Spell(val target: Entity) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			target.air = target.maxAir
			if (target is ServerPlayerEntity)
				target.networkHandler.sendPacket(PlaySoundS2CPacket(HexicalSounds.REPLENISH_AIR, SoundCategory.MASTER, target.x, target.y, target.z, 1f, 1f, 0))
		}
	}
}