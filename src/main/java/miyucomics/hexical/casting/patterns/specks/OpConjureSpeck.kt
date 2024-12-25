package miyucomics.hexical.casting.patterns.specks

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.entities.specklikes.SpeckEntity
import miyucomics.hexical.inits.HexicalAdvancements
import net.minecraft.command.argument.EntityAnchorArgumentType

class OpConjureSpeck : ConstMediaAction {
	override val argc = 3
	override val mediaCost = MediaConstants.DUST_UNIT / 100
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val iota = args[0]
		if (iota is PatternIota && iota.pattern == HexPattern.fromAngles("deaqq", HexDir.SOUTH_EAST))
			HexicalAdvancements.HEXXY.trigger(env.caster)

		val position = args.getVec3(1, argc)
		val rotation = args.getVec3(2, argc)
		env.assertVecInRange(position)

		val speck = SpeckEntity(env.world)
		speck.setPosition(position.subtract(0.0, speck.standingEyeHeight.toDouble(), 0.0))
		speck.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, speck.pos.add(rotation))
		speck.setPigment(env.pigment)
		speck.setIota(iota)
		env.world.spawnEntity(speck)

		HexicalAdvancements.AR.trigger(env.caster)
		return listOf(EntityIota(speck))
	}
}