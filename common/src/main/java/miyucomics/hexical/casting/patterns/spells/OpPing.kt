package miyucomics.hexical.casting.patterns.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d

class OpPing : SpellAction {
	override val argc = 1

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		return Triple(Spell(args.getVec3(0, argc)), 3 * MediaConstants.DUST_UNIT, listOf())
	}

	private data class Spell(val position: Vec3d) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val caster: ServerPlayerEntity = ctx.caster
			caster.networkHandler.sendPacket(ParticleS2CPacket(ParticleTypes.EXPLOSION_EMITTER, true, position.x, position.y, position.z, 0f, 0f, 0f, 0f, 1))
		}
	}
}