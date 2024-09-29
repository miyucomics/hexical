package miyucomics.hexical.casting.patterns.specks

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import miyucomics.hexical.casting.iota.getPigment
import miyucomics.hexical.interfaces.Specklike
import net.minecraft.command.argument.EntityAnchorArgumentType

class OpSpecklikeProperty(private val mode: Int) : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val specklike = args.getEntity(0, argc)
		if (specklike !is Specklike)
			throw MishapBadEntity.of(specklike, "speck_like")
		when (mode) {
			0 -> {
				val position = args.getVec3(1, argc)
				ctx.assertVecInRange(position)
				specklike.setPosition(position.subtract(0.0, specklike.standingEyeHeight.toDouble(), 0.0))
			}
			1 -> specklike.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, specklike.pos.add(args.getVec3(1, argc)))
			2 -> (specklike as Specklike).setRoll(args.getDoubleBetween(1, 0.0, 1.0, argc).toFloat() * 360)
			3 -> (specklike as Specklike).setSize(args.getPositiveDoubleUnderInclusive(1, 10.0, argc).toFloat())
			4 -> (specklike as Specklike).setThickness(args.getPositiveDoubleUnderInclusive(1, 10.0, argc).toFloat())
			5 -> (specklike as Specklike).setLifespan(args.getInt(1, argc))
			6 -> (specklike as Specklike).setPigment(args.getPigment(1, argc))
			else -> throw IllegalStateException()
		}
		return listOf()
	}
}