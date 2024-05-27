package miyucomics.hexical.casting.patterns.fireworks

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.entity.projectile.FireworkRocketEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.math.Vec3d

class OpConjureFirework : SpellAction {
	override val argc = 8
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val position = args.getVec3(0, argc)
		ctx.assertVecInRange(position)
		val velocity = args.getVec3(1, argc)
		val dyes = args.getList(2, argc)
		val fades = args.getList(3, argc)
		val shape = args.getIntBetween(3, 0, 4, argc)
		val gunpowder = args.getIntBetween(4, 1, 3, argc)
		val glowstone = args.getBool(5, argc)
		val diamond = args.getBool(6, argc)
		return Triple(Spell(position, velocity, dyes, fades, shape, gunpowder, glowstone, diamond), MediaConstants.SHARD_UNIT, listOf(ParticleSpray.burst(position, 1.0)))
	}

	private data class Spell(val position: Vec3d, val velocity: Vec3d, val dyes: SpellList, val fades: SpellList, val shape: Int, val gunpowder: Int, val glowstone: Boolean, val diamond: Boolean) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val stack = ItemStack(Items.FIREWORK_ROCKET)
			val firework = FireworkRocketEntity(ctx.world, stack, position.x, position.y, position.z, true)
			firework.setVelocity(velocity.x, velocity.y, velocity.z)
			ctx.world.spawnEntity(firework)
		}
	}
}