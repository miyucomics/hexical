package miyucomics.hexical.casting.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapNoSpellCircle
import miyucomics.hexical.casting.mishaps.OutsideDomainMishap
import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d

class OpDisplace : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		if (ctx.spellCircle == null)
			throw MishapNoSpellCircle()
		val entity = args.getEntity(0, argc)
		if (!ctx.spellCircle!!.aabb.contains(entity.pos))
			throw OutsideDomainMishap()
		val destination = args.getVec3(1, argc)
		if (!ctx.spellCircle!!.aabb.contains(destination))
			throw OutsideDomainMishap()
		return Triple(Spell(entity, destination), MediaConstants.SHARD_UNIT * 3, listOf())
	}

	private data class Spell(val entity: Entity, val destination: Vec3d) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			entity.teleport(destination.x, destination.y, destination.z)
		}
	}
}