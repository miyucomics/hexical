package miyucomics.hexical.features.mage_blocks

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import miyucomics.hexical.inits.HexicalAdvancements
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class OpConfigureMageBlock(private val modifier: MageBlockModifierType<*>) : SpellAction {
	override val argc = modifier.argc + 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getBlockPos(0, argc)
		env.assertPosInRange(position)
		if (env.world.getBlockState(position).block !is MageBlock)
			throw MishapBadBlock.of(position, "mage_block")
		val modifier = modifier.construct(args)
		return SpellAction.Result(Spell(position, modifier), 0, listOf(ParticleSpray.cloud(Vec3d.ofCenter(position), 1.0)))
	}

	private data class Spell(val pos: BlockPos, val modifier: MageBlockModifier) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			if (env.castingEntity is ServerPlayerEntity)
				HexicalAdvancements.DIY.trigger(env.castingEntity as ServerPlayerEntity)
			(env.world.getBlockEntity(pos) as MageBlockEntity).addModifier(modifier)
			env.world.updateNeighborsAlways(pos, HexicalBlocks.MAGE_BLOCK)
		}
	}
}