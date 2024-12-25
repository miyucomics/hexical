package miyucomics.hexical.casting.patterns.firework

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapBadOffhandItem
import net.minecraft.entity.projectile.FireworkRocketEntity
import net.minecraft.item.FireworkRocketItem
import net.minecraft.item.FireworkStarItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.util.math.Vec3d

class OpSimulateFirework : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val position = args.getVec3(0, argc)
		val duration = args.getIntBetween(2, 1, 3, argc)
		ctx.assertVecInRange(position)
		val star = ctx.caster.getStackInHand(ctx.otherHand)
		if (star.item !is FireworkStarItem)
			throw MishapBadOffhandItem.of(star, ctx.otherHand, "firework_star")
		return Triple(Spell(position, args.getVec3(1, argc), duration, star.orCreateNbt.getCompound(FireworkRocketItem.EXPLOSION_KEY)), MediaConstants.SHARD_UNIT + MediaConstants.DUST_UNIT * (duration - 1), listOf(ParticleSpray.burst(position, 1.0)))
	}

	private data class Spell(val position: Vec3d, val velocity: Vec3d, val duration: Int, val template: NbtCompound) : RenderedSpell {
		override fun cast(ctx: CastingEnvironment) {
			val star = NbtCompound()
			star.putInt(FireworkRocketItem.TYPE_KEY, template.getInt(FireworkRocketItem.TYPE_KEY))
			star.putByte(FireworkRocketItem.FLICKER_KEY, template.getByte(FireworkRocketItem.FLICKER_KEY))
			star.putByte(FireworkRocketItem.TRAIL_KEY, template.getByte(FireworkRocketItem.TRAIL_KEY))
			star.putIntArray(FireworkRocketItem.COLORS_KEY, template.getIntArray(FireworkRocketItem.COLORS_KEY))
			star.putIntArray(FireworkRocketItem.FADE_COLORS_KEY, template.getIntArray(FireworkRocketItem.FADE_COLORS_KEY))

			val explosions = NbtList()
			explosions.add(star)

			val main = NbtCompound()
			main.put(FireworkRocketItem.EXPLOSIONS_KEY, explosions)
			main.putByte(FireworkRocketItem.FLIGHT_KEY, duration.toByte())

			val stack = ItemStack(Items.FIREWORK_ROCKET)
			stack.orCreateNbt.put(FireworkRocketItem.FIREWORKS_KEY, main)

			val firework = FireworkRocketEntity(ctx.world, stack, position.x, position.y, position.z, true)
			firework.setVelocity(velocity.x, velocity.y, velocity.z)
			ctx.world.spawnEntity(firework)
		}
	}
}