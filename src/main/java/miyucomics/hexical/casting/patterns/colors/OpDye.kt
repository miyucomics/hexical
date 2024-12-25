package miyucomics.hexical.casting.patterns.colors

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.common.lib.HexItems
import miyucomics.hexical.casting.iota.getDye
import miyucomics.hexical.casting.iota.getTrueDye
import miyucomics.hexical.casting.mishaps.DyeableMishap
import miyucomics.hexical.data.DyeData
import miyucomics.hexical.interfaces.Specklike
import net.minecraft.block.*
import net.minecraft.block.entity.ShulkerBoxBlockEntity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.mob.ShulkerEntity
import net.minecraft.entity.passive.CatEntity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import java.util.*

class OpDye : SpellAction {
	override val argc = 2
	private val cost = MediaConstants.DUST_UNIT / 8
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val dye = args.getDye(1, argc)
		when (args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				env.assertEntityInRange(entity)
				return when (entity) {
					is CatEntity -> {
						val trueDye = args.getTrueDye(1, argc)
						SpellAction.Result(CatSpell(entity, trueDye), cost, listOf())
					}
					is SheepEntity -> {
						val trueDye = args.getTrueDye(1, argc)
						SpellAction.Result(SheepSpell(entity, trueDye), cost, listOf())
					}
					is ShulkerEntity -> {
						val trueDye = args.getTrueDye(1, argc)
						SpellAction.Result(ShulkerSpell(entity, trueDye), cost, listOf())
					}
					is Specklike -> {
						val trueDye = args.getTrueDye(1, argc)
						SpellAction.Result(SpecklikeSpell(entity, trueDye), cost, listOf())
					}
					is WolfEntity -> {
						val trueDye = args.getTrueDye(1, argc)
						SpellAction.Result(WolfSpell(entity, trueDye), cost, listOf())
					}
					is ItemEntity -> {
						when (val item = entity.stack.item) {
							is BlockItem -> {
								if (DyeData.isDyeable(item.block))
									SpellAction.Result(BlockItemSpell(entity, item.block, dye), cost, listOf())
								else
									throw DyeableMishap(entity.pos)
							}
							else -> {
								if (DyeData.isDyeable(item))
									SpellAction.Result(ItemSpell(entity, item, dye), cost, listOf())
								else
									throw DyeableMishap(entity.pos)
							}
						}
					}
					else -> throw DyeableMishap(entity.pos)
				}
			}
			is Vec3Iota -> {
				val position = args.getBlockPos(0, argc)
				env.assertPosInRange(position)
				val state = env.world.getBlockState(position)
				if (!DyeData.isDyeable(state.block))
					throw DyeableMishap(position.toCenterPos())
				return SpellAction.Result(BlockSpell(position, state, dye), cost, listOf())
			}
			else -> throw MishapInvalidIota.of(args[0], 0, "entity_or_vector")
		}
	}

	private data class BlockSpell(val position: BlockPos, val state: BlockState, val dye: String) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			when (state.block) {
				is CandleBlock -> env.world.setBlockState(
					position,
					DyeData.getNewBlock(state.block, dye)
						.with(CandleBlock.LIT, state.get(CandleBlock.LIT))
						.with(CandleBlock.CANDLES, state.get(CandleBlock.CANDLES))
				)
				is CandleCakeBlock -> env.world.setBlockState(
					position,
					DyeData.getNewBlock(state.block, dye)
						.with(CandleCakeBlock.LIT, state.get(CandleCakeBlock.LIT))
				)
				is GlazedTerracottaBlock -> env.world.setBlockState(
					position,
					DyeData.getNewBlock(state.block, dye)
						.with(GlazedTerracottaBlock.FACING, state.get(GlazedTerracottaBlock.FACING))
				)
				is SlabBlock -> env.world.setBlockState(
					position,
					DyeData.getNewBlock(state.block, dye)
						.with(SlabBlock.TYPE, state.get(SlabBlock.TYPE))
						.with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED))
				)
				is StairsBlock -> env.world.setBlockState(
					position,
					DyeData.getNewBlock(state.block, dye)
						.with(StairsBlock.FACING, state.get(StairsBlock.FACING))
						.with(StairsBlock.HALF, state.get(StairsBlock.HALF))
						.with(StairsBlock.SHAPE, state.get(StairsBlock.SHAPE))
						.with(StairsBlock.WATERLOGGED, state.get(StairsBlock.WATERLOGGED))
				)
				is WallBlock -> env.world.setBlockState(
					position,
					DyeData.getNewBlock(state.block, dye)
						.with(WallBlock.NORTH_SHAPE, state.get(WallBlock.NORTH_SHAPE))
						.with(WallBlock.EAST_SHAPE, state.get(WallBlock.EAST_SHAPE))
						.with(WallBlock.SOUTH_SHAPE, state.get(WallBlock.SOUTH_SHAPE))
						.with(WallBlock.WEST_SHAPE, state.get(WallBlock.WEST_SHAPE))
						.with(WallBlock.WATERLOGGED, state.get(WallBlock.WATERLOGGED))
						.with(WallBlock.UP, state.get(WallBlock.UP))
				)
				is ShulkerBoxBlock -> {
					val blockEntity = env.world.getBlockEntity(position)!! as ShulkerBoxBlockEntity
					val nbt = blockEntity.createNbt()
					env.world.setBlockState(
						position,
						DyeData.getNewBlock(state.block, dye)
							.with(ShulkerBoxBlock.FACING, state.get(ShulkerBoxBlock.FACING))
					)
					(env.world.getBlockEntity(position)!! as ShulkerBoxBlockEntity).readNbt(nbt)
				}
				is StainedGlassPaneBlock -> env.world.setBlockState(
					position,
					DyeData.getNewBlock(state.block, dye)
						.with(StainedGlassPaneBlock.NORTH, state.get(StainedGlassPaneBlock.NORTH))
						.with(StainedGlassPaneBlock.EAST, state.get(StainedGlassPaneBlock.EAST))
						.with(StainedGlassPaneBlock.SOUTH, state.get(StainedGlassPaneBlock.SOUTH))
						.with(StainedGlassPaneBlock.WEST, state.get(StainedGlassPaneBlock.WEST))
						.with(StainedGlassPaneBlock.WATERLOGGED, state.get(StainedGlassPaneBlock.WATERLOGGED))
				)
				else -> env.world.setBlockState(position, DyeData.getNewBlock(state.block, dye))
			}
		}
	}

	private data class BlockItemSpell(val item: ItemEntity, val block: Block, val dye: String) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val newStack = ItemStack(DyeData.getNewBlock(block, dye).block.asItem(), item.stack.count)
			newStack.nbt = item.stack.nbt
			item.stack = newStack
		}
	}

	private data class CatSpell(val cat: CatEntity, val dye: DyeColor) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			cat.collarColor = dye
		}
	}

	private data class ItemSpell(val entity: ItemEntity, val item: Item, val dye: String) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val newStack = ItemStack(DyeData.getNewItem(item, dye), entity.stack.count)
			newStack.nbt = entity.stack.nbt
			entity.stack = newStack
		}
	}

	private data class SheepSpell(val sheep: SheepEntity, val dye: DyeColor) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			sheep.color = dye
		}
	}

	private data class ShulkerSpell(val shulker: ShulkerEntity, val dye: DyeColor) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			shulker.variant = Optional.of(dye)
		}
	}

	private data class SpecklikeSpell(val speck: Specklike, val dye: DyeColor) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			speck.setPigment(FrozenPigment(ItemStack(HexItems.DYE_PIGMENTS[dye]!!), env.castingEntity!!.uuid))
		}
	}

	private data class WolfSpell(val wolf: WolfEntity, val dye: DyeColor) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			wolf.collarColor = dye
		}
	}
}