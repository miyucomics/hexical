package miyucomics.hexical.casting.spells.specks

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import miyucomics.hexical.entities.SpeckEntity
import net.minecraft.util.math.Vec3d

class OpMoveSpeck : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val entity = args.getEntity(0, argc)
		val position = args.getVec3(1, argc)
		ctx.assertVecInRange(position)
		if (entity !is SpeckEntity)
			throw MishapBadEntity.of(entity, "speck")
		return Triple(Spell(entity, position), 0, listOf())
	}

	private data class Spell(val speck: SpeckEntity, val position: Vec3d) : RenderedSpell {
		override fun cast(ctx: CastingContext) = speck.setPosition(position)
	}
}