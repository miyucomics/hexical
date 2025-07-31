package miyucomics.hexical.features.mage_blocks

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import miyucomics.hexical.inits.HexicalAdvancements
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class OpConfigureMageBlock(private val modifier: MageBlockModifier) : SpellAction {
	override val argc = modifier.argc + 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getBlockPos(0, argc)
		env.assertPosInRange(position)
		if (env.world.getBlockState(position).block !is MageBlock)
			throw MishapBadBlock.of(position, "mage_block")
		val arguments = modifier.getArguments(args)
		return SpellAction.Result(Spell(position, modifier, arguments), 0, listOf(ParticleSpray.cloud(Vec3d.ofCenter(position), 1.0)))
	}

	private data class Spell(val pos: BlockPos, val modifier: MageBlockModifier, val args: List<Any>) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			if (env.castingEntity is ServerPlayerEntity)
				HexicalAdvancements.DIY.trigger(env.castingEntity as ServerPlayerEntity)
			modifier.modify(args)
			env.world.updateNeighborsAlways(pos, HexicalBlocks.MAGE_BLOCK)
		}
	}
}