package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.entity.projectile.FireballEntity
import net.minecraft.util.math.Vec3d

class OpGhastFireball : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val pos = args.getVec3(0, argc)
		ctx.assertVecInRange(pos)
		return Triple(Spell(pos), MediaConstants.DUST_UNIT * 3, listOf())
	}

	private data class Spell(val position: Vec3d) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val fireball = FireballEntity(ctx.world, ctx.caster, 0.0, 0.0, 0.0, 1)
			fireball.setPosition(position)
			ctx.world.spawnEntity(fireball)
		}
	}
}