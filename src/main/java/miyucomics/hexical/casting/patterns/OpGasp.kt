package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.inits.HexicalSounds
import net.minecraft.entity.Entity
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory

class OpGasp : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val target = args.getEntity(0, argc)
		ctx.assertEntityInRange(target)
		return Triple(Spell(target), MediaConstants.DUST_UNIT, listOf(ParticleSpray.cloud(target.pos, 1.0)))
	}

	private data class Spell(val target: Entity) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			target.air = target.maxAir
			if (target is ServerPlayerEntity)
				target.networkHandler.sendPacket(PlaySoundS2CPacket(HexicalSounds.REPLENISH_AIR, SoundCategory.MASTER, target.x, target.y, target.z, 1f, 1f, 0))
		}
	}
}