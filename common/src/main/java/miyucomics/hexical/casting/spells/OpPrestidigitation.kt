package miyucomics.hexical.casting.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.util.math.Vec3d

class OpPrestidigitation : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val headOffset = args.getVec3(0, argc)
		return Triple(Spell(headOffset), MediaConstants.DUST_UNIT * 3, listOf())
	}

	private data class Spell(val position: Vec3d) : RenderedSpell {
		override fun cast(ctx: CastingContext) {

		}
	}
}