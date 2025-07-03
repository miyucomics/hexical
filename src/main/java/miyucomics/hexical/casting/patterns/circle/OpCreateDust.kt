package miyucomics.hexical.casting.patterns.circle

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.getPositiveInt
import at.petrak.hexcasting.api.casting.getPositiveIntUnderInclusive
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.circle.MishapNoSpellCircle
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.common.lib.HexItems
import miyucomics.hexical.casting.mishaps.OutsideCircleMishap
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Vec3d

class OpCreateDust : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env !is CircleCastEnv)
			throw MishapNoSpellCircle()

		val circle = env.impetus ?: throw MishapNoSpellCircle()
		val bounds = circle.executionState!!.bounds

		val position = args.getVec3(0, argc)
		if (!bounds.contains(position))
			throw OutsideCircleMishap()
		val amount = args.getPositiveIntUnderInclusive(1, 64, argc)

		return SpellAction.Result(Spell(position, amount), MediaConstants.DUST_UNIT * amount * 1.1, listOf(ParticleSpray.burst(position, 1.0)))
	}

	private data class Spell(val position: Vec3d, val amount: Int) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val item = ItemStack(HexItems.AMETHYST_DUST, amount)
			env.world.spawnEntity(ItemEntity(env.world, position.x, position.y, position.z, item))
		}
	}
}