package miyucomics.hexical.casting.patterns.scroll

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import miyucomics.hexical.entities.LivingScrollEntity
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.Vec3d
import kotlin.math.max
import kotlin.math.min

class OpColorScroll : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val scroll = args.getEntity(0, argc)
		env.assertEntityInRange(scroll)
		if (scroll !is LivingScrollEntity)
			throw MishapBadEntity.of(scroll, "living_scroll")
		val color = args.getVec3(1, argc)
		return SpellAction.Result(Spell(scroll, color), 0, listOf())
	}

	private data class Spell(val scroll: LivingScrollEntity, val color: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			scroll.setColor(
				ColorHelper.Argb.getArgb(
					1,
					(max(min(color.x, 1.0), 0.0) * 255).toInt(),
					(max(min(color.y, 1.0), 0.0) * 255).toInt(),
					(max(min(color.z, 1.0), 0.0) * 255).toInt()
				)
			)
		}
	}
}