package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.casting.iota.getIdentifier
import miyucomics.hexical.utils.ConstructedItemUsageContext
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.util.math.Direction

class OpPlaceBlockType : SpellAction {
	override val argc = 4
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val id = args.getIdentifier(0, argc)
		if (!Registries.BLOCK.containsId(id))
			throw MishapInvalidIota.of(args[0], 3, "block_id")
		val type = Registries.BLOCK.get(id)

		val stack = env.queryForMatchingStack { it.item is BlockItem && (it.item as BlockItem).block == type }
		if (stack == null)
			return SpellAction.Result(Noop(null), 0, listOf())

		val position = args.getBlockPos(1, argc)
		env.assertPosInRange(position)

		if (!IXplatAbstractions.INSTANCE.isPlacingAllowed(env.world, position, stack, null))
			return SpellAction.Result(Noop(null), 0, listOf())

		val direction = args.getBlockPos(2, argc)
		val normal = Direction.fromVector(direction.x, direction.y, direction.z) ?: throw MishapInvalidIota.of(args[1], 1, "axis_vector")

		val horizontal = args.getBlockPos(3, argc)
		val horizontalNormal = Direction.fromVector(horizontal.x, horizontal.y, horizontal.z) ?: throw MishapInvalidIota.of(args[3], 3, "horizontal_axis_vector")
		if (horizontalNormal.axis == Direction.Axis.Y)
			throw MishapInvalidIota.of(args[3], 0, "horizontal_axis_vector")

		val context = ConstructedItemUsageContext(env.world, position, normal, horizontalNormal, stack, env.castingHand)
		if (!env.world.getBlockState(position).canReplace(ItemPlacementContext(context)))
			throw MishapBadBlock.of(position, "replaceable")

		return SpellAction.Result(Spell(context, stack), MediaConstants.DUST_UNIT / 4, listOf())
	}

	private data class Noop(val item: ItemStack?) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {}
	}

	private data class Spell(val context: ConstructedItemUsageContext, val stack: ItemStack) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			stack.useOnBlock(context)
		}
	}
}