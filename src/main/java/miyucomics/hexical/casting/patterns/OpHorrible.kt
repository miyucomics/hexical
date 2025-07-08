package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import miyucomics.hexical.blocks.HexCandleCakeBlock
import miyucomics.hexical.blocks.HexCandleCakeBlockEntity
import miyucomics.hexical.registry.HexicalAdvancements
import miyucomics.hexical.registry.HexicalBlocks
import net.minecraft.block.AbstractCandleBlock
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class OpHorrible : SpellAction {
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