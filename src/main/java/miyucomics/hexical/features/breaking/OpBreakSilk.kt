package miyucomics.hexical.features.breaking

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.mod.HexConfig
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

object OpBreakSilk : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val pos = args.getBlockPos(0, argc)
		env.assertPosInRange(pos)
		return SpellAction.Result(Spell(pos), MediaConstants.DUST_UNIT / 2, listOf())
	}

	private data class Spell(val pos: BlockPos) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val state = env.world.getBlockState(pos)
			val tier = HexConfig.server().opBreakHarvestLevel()
			if (
				!state.isAir
                && state.getHardness(env.world, pos) >= 0f
                && IXplatAbstractions.INSTANCE.isCorrectTierForDrops(tier, state)
                && IXplatAbstractions.INSTANCE.isBreakingAllowed(env.world, pos, state, env.castingEntity as? ServerPlayerEntity)
			) {
				val blockEntity = env.world.getBlockEntity(pos)
				Block.dropStacks(state, env.world, pos, blockEntity, null, ItemStack(Items.DIAMOND_PICKAXE).apply { addEnchantment(Enchantments.SILK_TOUCH, 1) })
				env.world.breakBlock(pos, false)
			}
		}
	}
}
