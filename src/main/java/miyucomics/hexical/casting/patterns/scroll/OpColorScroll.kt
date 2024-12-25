package miyucomics.hexical.casting.patterns.scroll

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import miyucomics.hexical.entities.LivingScrollEntity
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.Vec3d
import kotlin.math.max
import kotlin.math.min

class OpColorScroll : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val scroll = args.getEntity(0, argc)
		ctx.assertEntityInRange(scroll)
		if (scroll !is LivingScrollEntity)
			throw MishapBadEntity.of(scroll, "living_scroll")
		val color = args.getVec3(1, argc)
		return Triple(Spell(scroll, color), 0, listOf())
	}

	private data class Spell(val scroll: LivingScrollEntity, val color: Vec3d) : RenderedSpell {
		override fun cast(ctx: CastingEnvironment) {
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