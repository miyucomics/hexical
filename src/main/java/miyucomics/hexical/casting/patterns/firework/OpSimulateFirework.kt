package miyucomics.hexical.casting.patterns.firework

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getIntBetween
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.entity.projectile.FireworkRocketEntity
import net.minecraft.item.FireworkRocketItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.util.math.Vec3d

class OpSimulateFirework : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getVec3(0, argc)
		val duration = args.getIntBetween(2, 1, 3, argc)
		env.assertVecInRange(position)

		val fireworkStar = env.getHeldItemToOperateOn { it.isOf(Items.FIREWORK_STAR) }
		if (fireworkStar == null)
			throw MishapBadOffhandItem.of(null, "firework_star")

		return SpellAction.Result(Spell(position, args.getVec3(1, argc), duration, fireworkStar.stack.orCreateNbt.getCompound(FireworkRocketItem.EXPLOSION_KEY)), MediaConstants.SHARD_UNIT, listOf(ParticleSpray.burst(position, 1.0)))
	}

	private data class Spell(val position: Vec3d, val velocity: Vec3d, val duration: Int, val template: NbtCompound) :
		RenderedSpell {
		override fun cast(env: CastingEnvironment) {
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

			val firework = FireworkRocketEntity(env.world, stack, position.x, position.y, position.z, true)
			firework.setVelocity(velocity.x, velocity.y, velocity.z)
			env.world.spawnEntity(firework)
		}
	}
}