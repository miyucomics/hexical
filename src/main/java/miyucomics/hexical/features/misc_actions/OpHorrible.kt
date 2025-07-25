package miyucomics.hexical.features.misc_actions

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import miyucomics.hexical.features.hex_candles.HexCandleCakeBlockEntity
import miyucomics.hexical.inits.HexicalAdvancements
import miyucomics.hexical.inits.HexicalBlocks
import net.minecraft.block.AbstractCandleBlock
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

object OpHorrible : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val pos = args.getBlockPos(0, argc)
		env.assertPosInRange(pos)
		if (!env.world.getBlockState(pos).isReplaceable)
			throw MishapBadBlock.of(pos, "replaceable")
		return SpellAction.Result(Spell(pos), 0, listOf(ParticleSpray.burst(Vec3d.ofCenter(pos), 2.0)))
	}

	private data class Spell(val pos: BlockPos) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			env.world.setBlockState(pos, HexicalBlocks.HEX_CANDLE_CAKE_BLOCK.defaultState.with(AbstractCandleBlock.LIT, true))
			(env.world.getBlockEntity(pos) as HexCandleCakeBlockEntity).setPigment(env.pigment)
			if (env.castingEntity is ServerPlayerEntity)
				HexicalAdvancements.CONJURE_CAKE.trigger(env.castingEntity as ServerPlayerEntity)
		}
	}
}