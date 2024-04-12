package miyucomics.hexical.casting.operators.specks

import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadEntity
import miyucomics.hexical.entities.SpeckEntity
import net.minecraft.command.argument.EntityAnchorArgumentType
import net.minecraft.util.math.Vec3d

class OpRotateSpeck : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val speck = args.getEntity(0, argc)
		val rotation = args.getVec3(1, argc)
		if (speck !is SpeckEntity)
			throw MishapBadEntity.of(speck, "speck")
		speck.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, speck.pos.add(rotation))
		return listOf()
	}
}