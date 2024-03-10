package miyucomics.hexical.casting.patterns.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.entities.SpeckEntity
import miyucomics.hexical.registry.HexicalEntities
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Vec3d

class OpConjureSpeck : SpellAction {
	override val argc = 2

	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val pattern = args.getPattern(0, argc)
		val position = args.getVec3(1, argc)
		ctx.assertVecInRange(position)
		return Triple(Spell(pattern, position), MediaConstants.DUST_UNIT / 1000, listOf())
	}

	private data class Spell(val pattern: HexPattern, val position: Vec3d) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val speck = SpeckEntity(HexicalEntities.SPECK_ENTITY, ctx.world)
			speck.setPosition(position)
			speck.setPattern(pattern)
			ctx.world.spawnEntity(speck)
		}
	}
}