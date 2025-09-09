package miyucomics.hexical.features.piston

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

object OpPiston : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getBlockPos(0, argc)
		env.assertPosInRange(position)
		val offset = args.getBlockPos(1, argc)
		val direction = Direction.fromVector(offset.x, offset.y, offset.z) ?: throw MishapInvalidIota.of(args[1], 1, "axis_vector")
		return SpellAction.Result(Spell(position, direction), MediaConstants.DUST_UNIT / 8, listOf(ParticleSpray.cloud(position.toCenterPos(), 1.0)))
	}

	private data class Spell(val position: BlockPos, val direction: Direction) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			BlockPusher.pushBlocks(env.world, position, direction)
		}
	}
}