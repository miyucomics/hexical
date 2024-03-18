package miyucomics.hexical.casting.spells.specks

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.entities.SpeckEntity
import miyucomics.hexical.registry.HexicalEntities
import net.minecraft.util.math.Vec3d

class OpConjureSpeck : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val iota = args[0]
		val position = args.getVec3(1, argc)
		ctx.assertVecInRange(position)
		return Triple(Spell(iota, position), MediaConstants.DUST_UNIT / 1000, listOf())
	}

	private data class Spell(val iota: Iota, val position: Vec3d) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val speck = SpeckEntity(HexicalEntities.SPECK_ENTITY, ctx.world)
			speck.setPosition(position)
			if (iota.type == PatternIota.TYPE)
				speck.setPattern((iota as PatternIota).pattern)
			else
				speck.setLabel(iota.display().string)
			speck.setPigment(IXplatAbstractions.INSTANCE.getColorizer(ctx.caster))
			speck.setSize(1f)
			speck.setThickness(1f)
			ctx.world.spawnEntity(speck)
		}
	}
}