package miyucomics.hexical.casting.patterns.specks

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.EntityIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.entities.specklikes.SpeckEntity
import miyucomics.hexical.inits.HexicalAdvancements
import net.minecraft.command.argument.EntityAnchorArgumentType

class OpConjureSpeck : ConstMediaAction {
	override val argc = 3
	override val mediaCost: Int = MediaConstants.DUST_UNIT / 100
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		val iota = args[0]
		if (iota is PatternIota && iota.pattern == HexPattern.fromAngles("deaqq", HexDir.SOUTH_EAST))
			HexicalAdvancements.HEXXY.trigger(ctx.caster)

		val position = args.getVec3(1, argc)
		val rotation = args.getVec3(2, argc)
		ctx.assertVecInRange(position)

		val speck = SpeckEntity(ctx.world)
		speck.setPosition(position.subtract(0.0, speck.standingEyeHeight.toDouble(), 0.0))
		speck.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, speck.pos.add(rotation))
		speck.setPigment(IXplatAbstractions.INSTANCE.getColorizer(ctx.caster))
		speck.setIota(iota)
		ctx.world.spawnEntity(speck)

		HexicalAdvancements.AR.trigger(ctx.caster)
		return listOf(EntityIota(speck))
	}
}