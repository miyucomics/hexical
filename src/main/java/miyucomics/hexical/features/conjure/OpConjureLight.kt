package miyucomics.hexical.features.conjure

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getPositiveIntUnderInclusive
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.block.Blocks
import net.minecraft.block.LightBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class OpConjureLight : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getBlockPos(0, argc)
		env.assertPosInRange(position)
		val power = args.getPositiveIntUnderInclusive(1, 15, argc)
		if (env.world.getBlockState(position).isOf(Blocks.LIGHT))
			return SpellAction.Result(Spell(position, power), 0, listOf(ParticleSpray.cloud(Vec3d.ofCenter(position), 1.0)))
		if (!env.world.getBlockState(position).isReplaceable)
			throw MishapBadBlock.of(position, "replaceable")
		return SpellAction.Result(Spell(position, power), MediaConstants.DUST_UNIT / 4, listOf(ParticleSpray.cloud(Vec3d.ofCenter(position), 1.0)))
	}

	private data class Spell(val position: BlockPos, val power: Int) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			env.world.setBlockState(position, Blocks.LIGHT.defaultState.with(LightBlock.LEVEL_15, power))
		}
	}
}