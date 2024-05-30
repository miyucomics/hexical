package miyucomics.hexical.casting.patterns.dye

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.EntityIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import miyucomics.hexical.casting.mishaps.DyeableMishap
import miyucomics.hexical.data.DyeData
import miyucomics.hexical.iota.getDye
import net.minecraft.block.BlockState
import net.minecraft.block.CandleBlock
import net.minecraft.block.ShulkerBoxBlock
import net.minecraft.block.StainedGlassPaneBlock
import net.minecraft.block.entity.ShulkerBoxBlockEntity
import net.minecraft.entity.mob.ShulkerEntity
import net.minecraft.entity.passive.CatEntity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos

class OpDye : SpellAction {
	override val argc = 2
	private val cost = MediaConstants.DUST_UNIT / 8
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val dye = args.getDye(1, argc)
		when (args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				ctx.assertEntityInRange(entity)
				return when (entity) {
					is CatEntity -> Triple(CatSpell(entity, dye), cost, listOf())
					is SheepEntity -> Triple(SheepSpell(entity, dye), cost, listOf())
					is ShulkerEntity -> Triple(ShulkerSpell(entity, dye), cost, listOf())
					is WolfEntity -> Triple(WolfSpell(entity, dye), cost, listOf())
					else -> throw DyeableMishap()
				}
			}
			is Vec3Iota -> {
				val position = args.getBlockPos(0, argc)
				ctx.assertVecInRange(position)
				val state = ctx.world.getBlockState(position)
				if (!DyeData.isDyeable(state.block))
					throw DyeableMishap()
				return Triple(BlockSpell(position, state, dye), cost, listOf())
			}
			else -> throw DyeableMishap()
		}
	}

	private data class BlockSpell(val position: BlockPos, val state: BlockState, val dye: DyeColor) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			when (state.block) {
				is CandleBlock -> ctx.world.setBlockState(
					position,
					DyeData.getNewBlock(state.block, dye)
						.with(CandleBlock.LIT, state.get(CandleBlock.LIT))
						.with(CandleBlock.CANDLES, state.get(CandleBlock.CANDLES))
				)
				is ShulkerBoxBlock -> {
					val blockEntity = ctx.world.getBlockEntity(position)!! as ShulkerBoxBlockEntity
					val inventoryNbt = NbtCompound()
					blockEntity.readInventoryNbt(inventoryNbt)
					print(inventoryNbt)
					ctx.world.setBlockState(
						position,
						DyeData.getNewBlock(state.block, dye)
							.with(ShulkerBoxBlock.FACING, state.get(ShulkerBoxBlock.FACING))
					)
				}
				is StainedGlassPaneBlock -> ctx.world.setBlockState(
					position,
					DyeData.getNewBlock(state.block, dye)
						.with(StainedGlassPaneBlock.NORTH, state.get(StainedGlassPaneBlock.NORTH))
						.with(StainedGlassPaneBlock.EAST, state.get(StainedGlassPaneBlock.EAST))
						.with(StainedGlassPaneBlock.SOUTH, state.get(StainedGlassPaneBlock.SOUTH))
						.with(StainedGlassPaneBlock.WEST, state.get(StainedGlassPaneBlock.WEST))
						.with(StainedGlassPaneBlock.WATERLOGGED, state.get(StainedGlassPaneBlock.WATERLOGGED))
				)
				else -> ctx.world.setBlockState(position, DyeData.getNewBlock(state.block, dye))
			}
		}
	}

	private data class SheepSpell(val sheep: SheepEntity, val dye: DyeColor) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			sheep.color = dye
		}
	}

	private data class ShulkerSpell(val shulker: ShulkerEntity, val dye: DyeColor) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			shulker.color = dye
		}
	}

	private data class CatSpell(val cat: CatEntity, val dye: DyeColor) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			cat.collarColor = dye
		}
	}

	private data class WolfSpell(val wolf: WolfEntity, val dye: DyeColor) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			wolf.collarColor = dye
		}
	}
}