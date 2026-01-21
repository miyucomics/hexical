package miyucomics.hexical.features.misc_actions

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.entity.projectile.FireworkRocketEntity
import net.minecraft.item.FireworkRocketItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.Vec3d

object OpConjureFirework : SpellAction {
	override val argc = 8
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getVec3(0, argc)
		env.assertVecInRange(position)

		val velocity = args.getVec3(1, argc)
		val duration = args.getIntBetween(2, 1, 3, argc)
		val shape = args.getPositiveIntUnderInclusive(3, 4, argc)

		val colors = args.getList(4, argc)
		if (!colors.nonEmpty)
			throw MishapInvalidIota.of(args[3], 3, "nonempty_list")
		val trueColors = colors.map {
			if (it !is Vec3Iota)
				throw MishapInvalidIota.of(args[3], 3, "vector_list")
			translateVectorToColor(it.vec3)
		}

		val fades = args.getList(5, argc).map {
			if (it !is Vec3Iota)
				throw MishapInvalidIota.of(args[3], 2, "vector_list")
			translateVectorToColor(it.vec3)
		}

		val flicker = args.getBool(6, argc)
		val trail = args.getBool(7, argc)

		return SpellAction.Result(Spell(position, velocity, duration, shape, trueColors, fades, flicker, trail), MediaConstants.SHARD_UNIT, listOf(ParticleSpray.burst(position, 1.0)))
	}

	private data class Spell(val position: Vec3d, val desiredVelocity: Vec3d, val duration: Int, val shape: Int, val colors: List<Int>, val fades: List<Int>, val flicker: Boolean, val trail: Boolean) :
		RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val fireworkNbt = NbtCompound().apply {
				put(FireworkRocketItem.EXPLOSIONS_KEY, NbtList().apply {
					add(NbtCompound().apply {
						putInt(FireworkRocketItem.TYPE_KEY, shape)
						putIntArray(FireworkRocketItem.COLORS_KEY, colors)
						if (fades.isNotEmpty()) putIntArray(FireworkRocketItem.FADE_COLORS_KEY, fades)
						if (flicker) putByte(FireworkRocketItem.FLICKER_KEY, 0b1)
						if (trail) putByte(FireworkRocketItem.TRAIL_KEY, 0b1)
					})
				})
				putByte(FireworkRocketItem.FLIGHT_KEY, duration.toByte())
			}

			val fireworkStack = ItemStack(Items.FIREWORK_ROCKET).also { stack ->
				stack.orCreateNbt.put(FireworkRocketItem.FIREWORKS_KEY, fireworkNbt)
			}

			env.world.spawnEntity(
				FireworkRocketEntity(
					env.world,
					fireworkStack,
					position.x,
					position.y,
					position.z,
					true
				).apply {
				setVelocity(desiredVelocity.x, desiredVelocity.y, desiredVelocity.z)
			})
		}
	}

	private fun translateVectorToColor(vector: Vec3d): Int {
		return ColorHelper.Argb.getArgb(255, (vector.x.coerceIn(0.0, 1.0) * 255).toInt(), (vector.y.coerceIn(0.0, 1.0) * 255).toInt(), (vector.z.coerceIn(0.0, 1.0) * 255).toInt())
	}
}