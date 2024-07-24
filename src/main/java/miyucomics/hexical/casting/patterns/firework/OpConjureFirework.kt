package miyucomics.hexical.casting.patterns.firework

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.casting.iota.DyeIota
import net.minecraft.entity.projectile.FireworkRocketEntity
import net.minecraft.item.FireworkRocketItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.util.DyeColor
import net.minecraft.util.math.Vec3d

class OpConjureFirework : SpellAction {
	override val argc = 8
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val position = args.getVec3(0, argc)
		ctx.assertVecInRange(position)

		val velocity = args.getVec3(1, argc)
		val duration = args.getIntBetween(2, 1, 3, argc)
		val type = args.getIntBetween(3, 0, 4, argc)

		val colors = args.getList(4, argc)
		if (!colors.nonEmpty)
			throw MishapInvalidIota.of(args[3], 3, "nonempty_list")
		val trueColors = mutableListOf<Int>()
		for (color in colors) {
			if (color.type != DyeIota.TYPE)
				throw MishapInvalidIota.of(args[3], 3, "true_dye_list")
			trueColors.add(DyeColor.byName((color as DyeIota).dye, DyeColor.WHITE)!!.fireworkColor)
		}

		val fades = args.getList(5, argc)
		val trueFades = mutableListOf<Int>()
		for (fade in fades) {
			if (fade.type != DyeIota.TYPE)
				throw MishapInvalidIota.of(args[4], 4, "true_dye_list")
			trueFades.add(DyeColor.byName((fade as DyeIota).dye, DyeColor.WHITE)!!.fireworkColor)
		}

		val flicker = args.getBool(6, argc)
		val trail = args.getBool(7, argc)

		return Triple(Spell(position, velocity, duration, type, trueColors, trueFades, flicker, trail), MediaConstants.SHARD_UNIT + MediaConstants.DUST_UNIT * (duration - 1), listOf(ParticleSpray.burst(position, 1.0)))
	}

	private data class Spell(val position: Vec3d, val velocity: Vec3d, val duration: Int, val type: Int, val colors: List<Int>, val fades: List<Int>, val flicker: Boolean, val trail: Boolean) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val star = NbtCompound()
			star.putInt(FireworkRocketItem.TYPE_KEY, type)
			if (flicker)
				star.putByte(FireworkRocketItem.FLICKER_KEY, (1).toByte())
			if (trail)
				star.putByte(FireworkRocketItem.TRAIL_KEY, (1).toByte())
			star.putIntArray(FireworkRocketItem.COLORS_KEY, colors)
			if (fades.isNotEmpty())
				star.putIntArray(FireworkRocketItem.FADE_COLORS_KEY, fades)

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