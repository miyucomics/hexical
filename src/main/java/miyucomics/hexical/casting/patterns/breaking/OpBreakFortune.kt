package miyucomics.hexical.casting.patterns.breaking

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getPositiveIntUnderInclusive
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.math.BlockPos

class OpBreakFortune : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val pos = args.getBlockPos(0, argc)
		env.assertPosInRange(pos)
		val level = args.getPositiveIntUnderInclusive(1, 2, argc)
		return SpellAction.Result(Spell(pos, level), MediaConstants.DUST_UNIT + level * MediaConstants.DUST_UNIT * 2, listOf())
	}

	private data class Spell(val pos: BlockPos, val level: Int) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val state = env.world.getBlockState(pos)
			if (state.isAir)
				return
			val tool = ItemStack(Items.DIAMOND_PICKAXE)
			tool.addEnchantment(Enchantments.FORTUNE, level + 1)
			Block.dropStacks(state, env.world, pos, null, null, tool)
			env.world.breakBlock(pos, false)
		}
	}
}