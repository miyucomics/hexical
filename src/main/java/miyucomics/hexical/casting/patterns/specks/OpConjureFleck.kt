package miyucomics.hexical.casting.patterns.specks

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getList
import at.petrak.hexcasting.api.spell.getVec3
import at.petrak.hexcasting.api.spell.iota.EntityIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.entities.FleckEntity
import miyucomics.hexical.registry.HexicalAdvancements
import miyucomics.hexical.registry.HexicalEntities
import net.minecraft.command.argument.EntityAnchorArgumentType
import net.minecraft.util.math.Vec2f

class OpConjureFleck : ConstMediaAction {
	override val argc = 3
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val points = args.getList(0, argc)
		val constructedPoints = mutableListOf<Vec2f>()
		for (point in points) {
			if (point.type != Vec3Iota.TYPE)
				throw MishapInvalidIota.of(args[0], 0, "vector_list")
			else {
				val vector = (point as Vec3Iota).vec3
				constructedPoints.add(Vec2f(vector.x.toFloat(), vector.y.toFloat()))
			}
		}

		val position = args.getVec3(1, argc)
		val rotation = args.getVec3(2, argc)
		ctx.assertVecInRange(position)
		val fleck = FleckEntity(HexicalEntities.FLECK_ENTITY, ctx.world)
		fleck.setPosition(position)
		fleck.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, fleck.pos.add(rotation))
		fleck.setPigment(IXplatAbstractions.INSTANCE.getColorizer(ctx.caster))
		fleck.setShape(constructedPoints)
		fleck.setSize(1f)
		fleck.setThickness(1f)
		ctx.world.spawnEntity(fleck)
		HexicalAdvancements.AR.trigger(ctx.caster)
		return listOf(EntityIota(fleck))
	}
}