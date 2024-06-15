package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.EntityIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d
import java.lang.IllegalStateException

class OpPrestidigitation : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		return when (args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				Triple(EntitySpell(entity), MediaConstants.DUST_UNIT * 3, listOf())
			}
			is Vec3Iota -> {
				val position = args.getVec3(0, argc)
				Triple(BlockSpell(position), MediaConstants.DUST_UNIT * 3, listOf())
			}
			else -> throw IllegalStateException()
		}
	}

	private data class BlockSpell(val position: Vec3d) : RenderedSpell {
		override fun cast(ctx: CastingContext) {

		}
	}

	private data class EntitySpell(val entity: Entity) : RenderedSpell {
		override fun cast(ctx: CastingContext) {

		}
	}
}