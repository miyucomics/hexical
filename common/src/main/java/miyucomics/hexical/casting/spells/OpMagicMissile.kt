package miyucomics.hexical.casting.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.entity.projectile.thrown.SnowballEntity
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

class OpMagicMissile : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val headCoords = args.getVec3(0, argc)
		val xAxis = ctx.caster.rotationVector
		val f = (-ctx.caster.pitch + 90) * (Math.PI.toFloat() / 180)
		val g = -ctx.caster.headYaw * (Math.PI.toFloat() / 180)
		val h = MathHelper.cos(g).toDouble()
		val i = MathHelper.sin(g).toDouble()
		val j = MathHelper.cos(f).toDouble()
		val k = MathHelper.sin(f).toDouble()
		val yAxis = Vec3d(i * j, k, h * j)
		val zAxis = xAxis.crossProduct(yAxis).normalize()
		val worldCoords = ctx.caster.eyePos
			.add(xAxis.multiply(headCoords.x))
			.add(yAxis.multiply(headCoords.y))
			.add(zAxis.multiply(headCoords.z))
		ctx.assertVecInRange(worldCoords)

		val velocity = args.getVec3(1, argc)

		return Triple(Spell(worldCoords, velocity), MediaConstants.DUST_UNIT, listOf())
	}

	private data class Spell(val position: Vec3d, val velocity: Vec3d) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val snowball = SnowballEntity(ctx.world, ctx.caster)
			snowball.setPos(position.x, position.y, position.z)
			snowball.setVelocity(velocity.x, velocity.y, velocity.z)
			ctx.world.spawnEntity(snowball)
		}
	}
}