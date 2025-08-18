package miyucomics.hexical.features.mage_blocks

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

object OpResetMageBlock : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getBlockPos(0, argc)
		env.assertPosInRange(position)
		if (env.world.getBlockState(position).block !is MageBlock)
			throw MishapBadBlock.of(position, "mage_block")
		return SpellAction.Result(Spell(position), 0, listOf(ParticleSpray.cloud(Vec3d.ofCenter(position), 1.0)))
	}

	private data class Spell(val pos: BlockPos) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			(env.world.getBlockEntity(pos) as MageBlockEntity).clearModifiers()
		}
	}
}