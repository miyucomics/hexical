package miyucomics.hexical.features.amber_seal

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.Block
import net.minecraft.util.math.BlockPos

object OpAmberSeal : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getBlockPos(0, argc)
		env.assertPosInRange(position)
		return SpellAction.Result(Spell(position), MediaConstants.CRYSTAL_UNIT, listOf())
	}

	private data class Spell(val position: BlockPos) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val originalState = env.world.getBlockState(position)
			val originalEntity = env.world.getBlockEntity(position)
			env.world.removeBlockEntity(position)
			env.world.setBlockState(position, AmberSealBlock.copyRotationParameters(HexicalBlocks.AMBER_SEAL_BLOCK.defaultState, env.world.getBlockState(position)), Block.NOTIFY_ALL or Block.REDRAW_ON_MAIN_THREAD)
			(env.world.getBlockEntity(position) as AmberSealBlockEntity).init(originalState, originalEntity)
		}
	}
}