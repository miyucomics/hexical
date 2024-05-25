package miyucomics.hexical.casting.operators

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getBlockPos
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.EntityIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import miyucomics.hexical.iota.DyeIota
import miyucomics.hexical.registry.HexicalTags
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.SignBlock
import net.minecraft.block.entity.SignBlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.DyeItem
import net.minecraft.server.world.ServerWorld
import net.minecraft.tag.BlockTags
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos

class OpGetDye : ConstMediaAction {
	private val beds = mapOf(
		Blocks.WHITE_BED to DyeColor.WHITE,
		Blocks.ORANGE_BED to DyeColor.ORANGE,
		Blocks.MAGENTA_BED to DyeColor.MAGENTA,
		Blocks.LIGHT_BLUE_BED to DyeColor.LIGHT_BLUE,
		Blocks.YELLOW_BED to DyeColor.YELLOW,
		Blocks.LIME_BED to DyeColor.LIME,
		Blocks.PINK_BED to DyeColor.PINK,
		Blocks.GRAY_BED to DyeColor.GRAY,
		Blocks.LIGHT_GRAY_BED to DyeColor.LIGHT_GRAY,
		Blocks.CYAN_BED to DyeColor.CYAN,
		Blocks.PURPLE_BED to DyeColor.PURPLE,
		Blocks.BLUE_BED to DyeColor.BLUE,
		Blocks.BROWN_BED to DyeColor.BROWN,
		Blocks.GREEN_BED to DyeColor.GREEN,
		Blocks.RED_BED to DyeColor.RED,
		Blocks.BLACK_BED to DyeColor.BLACK
	)
	private val cakeCandles = mapOf(
		Blocks.WHITE_CANDLE_CAKE to DyeColor.WHITE,
		Blocks.ORANGE_CANDLE_CAKE to DyeColor.ORANGE,
		Blocks.MAGENTA_CANDLE_CAKE to DyeColor.MAGENTA,
		Blocks.LIGHT_BLUE_CANDLE_CAKE to DyeColor.LIGHT_BLUE,
		Blocks.YELLOW_CANDLE_CAKE to DyeColor.YELLOW,
		Blocks.LIME_CANDLE_CAKE to DyeColor.LIME,
		Blocks.PINK_CANDLE_CAKE to DyeColor.PINK,
		Blocks.GRAY_CANDLE_CAKE to DyeColor.GRAY,
		Blocks.LIGHT_GRAY_CANDLE_CAKE to DyeColor.LIGHT_GRAY,
		Blocks.CYAN_CANDLE_CAKE to DyeColor.CYAN,
		Blocks.PURPLE_CANDLE_CAKE to DyeColor.PURPLE,
		Blocks.BLUE_CANDLE_CAKE to DyeColor.BLUE,
		Blocks.BROWN_CANDLE_CAKE to DyeColor.BROWN,
		Blocks.GREEN_CANDLE_CAKE to DyeColor.GREEN,
		Blocks.RED_CANDLE_CAKE to DyeColor.RED,
		Blocks.BLACK_CANDLE_CAKE to DyeColor.BLACK
	)
	private val candles = mapOf(
		Blocks.WHITE_CANDLE to DyeColor.WHITE,
		Blocks.ORANGE_CANDLE to DyeColor.ORANGE,
		Blocks.MAGENTA_CANDLE to DyeColor.MAGENTA,
		Blocks.LIGHT_BLUE_CANDLE to DyeColor.LIGHT_BLUE,
		Blocks.YELLOW_CANDLE to DyeColor.YELLOW,
		Blocks.LIME_CANDLE to DyeColor.LIME,
		Blocks.PINK_CANDLE to DyeColor.PINK,
		Blocks.GRAY_CANDLE to DyeColor.GRAY,
		Blocks.LIGHT_GRAY_CANDLE to DyeColor.LIGHT_GRAY,
		Blocks.CYAN_CANDLE to DyeColor.CYAN,
		Blocks.PURPLE_CANDLE to DyeColor.PURPLE,
		Blocks.BLUE_CANDLE to DyeColor.BLUE,
		Blocks.BROWN_CANDLE to DyeColor.BROWN,
		Blocks.GREEN_CANDLE to DyeColor.GREEN,
		Blocks.RED_CANDLE to DyeColor.RED,
		Blocks.BLACK_CANDLE to DyeColor.BLACK
	)
	private val carpets = mapOf(
		Blocks.WHITE_CARPET to DyeColor.WHITE,
		Blocks.ORANGE_CARPET to DyeColor.ORANGE,
		Blocks.MAGENTA_CARPET to DyeColor.MAGENTA,
		Blocks.LIGHT_BLUE_CARPET to DyeColor.LIGHT_BLUE,
		Blocks.YELLOW_CARPET to DyeColor.YELLOW,
		Blocks.LIME_CARPET to DyeColor.LIME,
		Blocks.PINK_CARPET to DyeColor.PINK,
		Blocks.GRAY_CARPET to DyeColor.GRAY,
		Blocks.LIGHT_GRAY_CARPET to DyeColor.LIGHT_GRAY,
		Blocks.CYAN_CARPET to DyeColor.CYAN,
		Blocks.PURPLE_CARPET to DyeColor.PURPLE,
		Blocks.BLUE_CARPET to DyeColor.BLUE,
		Blocks.BROWN_CARPET to DyeColor.BROWN,
		Blocks.GREEN_CARPET to DyeColor.GREEN,
		Blocks.RED_CARPET to DyeColor.RED,
		Blocks.BLACK_CARPET to DyeColor.BLACK
	)
	private val concretes = mapOf(
		Blocks.WHITE_CONCRETE to DyeColor.WHITE,
		Blocks.ORANGE_CONCRETE to DyeColor.ORANGE,
		Blocks.MAGENTA_CONCRETE to DyeColor.MAGENTA,
		Blocks.LIGHT_BLUE_CONCRETE to DyeColor.LIGHT_BLUE,
		Blocks.YELLOW_CONCRETE to DyeColor.YELLOW,
		Blocks.LIME_CONCRETE to DyeColor.LIME,
		Blocks.PINK_CONCRETE to DyeColor.PINK,
		Blocks.GRAY_CONCRETE to DyeColor.GRAY,
		Blocks.LIGHT_GRAY_CONCRETE to DyeColor.LIGHT_GRAY,
		Blocks.CYAN_CONCRETE to DyeColor.CYAN,
		Blocks.PURPLE_CONCRETE to DyeColor.PURPLE,
		Blocks.BLUE_CONCRETE to DyeColor.BLUE,
		Blocks.BROWN_CONCRETE to DyeColor.BROWN,
		Blocks.GREEN_CONCRETE to DyeColor.GREEN,
		Blocks.RED_CONCRETE to DyeColor.RED,
		Blocks.BLACK_CONCRETE to DyeColor.BLACK
	)
	private val concretePowders = mapOf(
		Blocks.WHITE_CONCRETE_POWDER to DyeColor.WHITE,
		Blocks.ORANGE_CONCRETE_POWDER to DyeColor.ORANGE,
		Blocks.MAGENTA_CONCRETE_POWDER to DyeColor.MAGENTA,
		Blocks.LIGHT_BLUE_CONCRETE_POWDER to DyeColor.LIGHT_BLUE,
		Blocks.YELLOW_CONCRETE_POWDER to DyeColor.YELLOW,
		Blocks.LIME_CONCRETE_POWDER to DyeColor.LIME,
		Blocks.PINK_CONCRETE_POWDER to DyeColor.PINK,
		Blocks.GRAY_CONCRETE_POWDER to DyeColor.GRAY,
		Blocks.LIGHT_GRAY_CONCRETE_POWDER to DyeColor.LIGHT_GRAY,
		Blocks.CYAN_CONCRETE_POWDER to DyeColor.CYAN,
		Blocks.PURPLE_CONCRETE_POWDER to DyeColor.PURPLE,
		Blocks.BLUE_CONCRETE_POWDER to DyeColor.BLUE,
		Blocks.BROWN_CONCRETE_POWDER to DyeColor.BROWN,
		Blocks.GREEN_CONCRETE_POWDER to DyeColor.GREEN,
		Blocks.RED_CONCRETE_POWDER to DyeColor.RED,
		Blocks.BLACK_CONCRETE_POWDER to DyeColor.BLACK
	)
	private val glazedTerracotta = mapOf(
		Blocks.WHITE_GLAZED_TERRACOTTA to DyeColor.WHITE,
		Blocks.ORANGE_GLAZED_TERRACOTTA to DyeColor.ORANGE,
		Blocks.MAGENTA_GLAZED_TERRACOTTA to DyeColor.MAGENTA,
		Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA to DyeColor.LIGHT_BLUE,
		Blocks.YELLOW_GLAZED_TERRACOTTA to DyeColor.YELLOW,
		Blocks.LIME_GLAZED_TERRACOTTA to DyeColor.LIME,
		Blocks.PINK_GLAZED_TERRACOTTA to DyeColor.PINK,
		Blocks.GRAY_GLAZED_TERRACOTTA to DyeColor.GRAY,
		Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA to DyeColor.LIGHT_GRAY,
		Blocks.CYAN_GLAZED_TERRACOTTA to DyeColor.CYAN,
		Blocks.PURPLE_GLAZED_TERRACOTTA to DyeColor.PURPLE,
		Blocks.BLUE_GLAZED_TERRACOTTA to DyeColor.BLUE,
		Blocks.BROWN_GLAZED_TERRACOTTA to DyeColor.BROWN,
		Blocks.GREEN_GLAZED_TERRACOTTA to DyeColor.GREEN,
		Blocks.RED_GLAZED_TERRACOTTA to DyeColor.RED,
		Blocks.BLACK_GLAZED_TERRACOTTA to DyeColor.BLACK
	)
	private val shulkerBoxes = mapOf(
		Blocks.WHITE_SHULKER_BOX to DyeColor.WHITE,
		Blocks.ORANGE_SHULKER_BOX to DyeColor.ORANGE,
		Blocks.MAGENTA_SHULKER_BOX to DyeColor.MAGENTA,
		Blocks.LIGHT_BLUE_SHULKER_BOX to DyeColor.LIGHT_BLUE,
		Blocks.YELLOW_SHULKER_BOX to DyeColor.YELLOW,
		Blocks.LIME_SHULKER_BOX to DyeColor.LIME,
		Blocks.PINK_SHULKER_BOX to DyeColor.PINK,
		Blocks.GRAY_SHULKER_BOX to DyeColor.GRAY,
		Blocks.LIGHT_GRAY_SHULKER_BOX to DyeColor.LIGHT_GRAY,
		Blocks.CYAN_SHULKER_BOX to DyeColor.CYAN,
		Blocks.PURPLE_SHULKER_BOX to DyeColor.PURPLE,
		Blocks.BLUE_SHULKER_BOX to DyeColor.BLUE,
		Blocks.BROWN_SHULKER_BOX to DyeColor.BROWN,
		Blocks.GREEN_SHULKER_BOX to DyeColor.GREEN,
		Blocks.RED_SHULKER_BOX to DyeColor.RED,
		Blocks.BLACK_SHULKER_BOX to DyeColor.BLACK
	)
	private val stainedGlass = mapOf(
		Blocks.WHITE_STAINED_GLASS to DyeColor.WHITE,
		Blocks.ORANGE_STAINED_GLASS to DyeColor.ORANGE,
		Blocks.MAGENTA_STAINED_GLASS to DyeColor.MAGENTA,
		Blocks.LIGHT_BLUE_STAINED_GLASS to DyeColor.LIGHT_BLUE,
		Blocks.YELLOW_STAINED_GLASS to DyeColor.YELLOW,
		Blocks.LIME_STAINED_GLASS to DyeColor.LIME,
		Blocks.PINK_STAINED_GLASS to DyeColor.PINK,
		Blocks.GRAY_STAINED_GLASS to DyeColor.GRAY,
		Blocks.LIGHT_GRAY_STAINED_GLASS to DyeColor.LIGHT_GRAY,
		Blocks.CYAN_STAINED_GLASS to DyeColor.CYAN,
		Blocks.PURPLE_STAINED_GLASS to DyeColor.PURPLE,
		Blocks.BLUE_STAINED_GLASS to DyeColor.BLUE,
		Blocks.BROWN_STAINED_GLASS to DyeColor.BROWN,
		Blocks.GREEN_STAINED_GLASS to DyeColor.GREEN,
		Blocks.RED_STAINED_GLASS to DyeColor.RED,
		Blocks.BLACK_STAINED_GLASS to DyeColor.BLACK
	)
	private val stainedGlassPanes = mapOf(
		Blocks.WHITE_STAINED_GLASS_PANE to DyeColor.WHITE,
		Blocks.ORANGE_STAINED_GLASS_PANE to DyeColor.ORANGE,
		Blocks.MAGENTA_STAINED_GLASS_PANE to DyeColor.MAGENTA,
		Blocks.LIGHT_BLUE_STAINED_GLASS_PANE to DyeColor.LIGHT_BLUE,
		Blocks.YELLOW_STAINED_GLASS_PANE to DyeColor.YELLOW,
		Blocks.LIME_STAINED_GLASS_PANE to DyeColor.LIME,
		Blocks.PINK_STAINED_GLASS_PANE to DyeColor.PINK,
		Blocks.GRAY_STAINED_GLASS_PANE to DyeColor.GRAY,
		Blocks.LIGHT_GRAY_STAINED_GLASS_PANE to DyeColor.LIGHT_GRAY,
		Blocks.CYAN_STAINED_GLASS_PANE to DyeColor.CYAN,
		Blocks.PURPLE_STAINED_GLASS_PANE to DyeColor.PURPLE,
		Blocks.BLUE_STAINED_GLASS_PANE to DyeColor.BLUE,
		Blocks.BROWN_STAINED_GLASS_PANE to DyeColor.BROWN,
		Blocks.GREEN_STAINED_GLASS_PANE to DyeColor.GREEN,
		Blocks.RED_STAINED_GLASS_PANE to DyeColor.RED,
		Blocks.BLACK_STAINED_GLASS_PANE to DyeColor.BLACK
	)
	private val terracotta = mapOf(
		Blocks.WHITE_TERRACOTTA to DyeColor.WHITE,
		Blocks.ORANGE_TERRACOTTA to DyeColor.ORANGE,
		Blocks.MAGENTA_TERRACOTTA to DyeColor.MAGENTA,
		Blocks.LIGHT_BLUE_TERRACOTTA to DyeColor.LIGHT_BLUE,
		Blocks.YELLOW_TERRACOTTA to DyeColor.YELLOW,
		Blocks.LIME_TERRACOTTA to DyeColor.LIME,
		Blocks.PINK_TERRACOTTA to DyeColor.PINK,
		Blocks.GRAY_TERRACOTTA to DyeColor.GRAY,
		Blocks.LIGHT_GRAY_TERRACOTTA to DyeColor.LIGHT_GRAY,
		Blocks.CYAN_TERRACOTTA to DyeColor.CYAN,
		Blocks.PURPLE_TERRACOTTA to DyeColor.PURPLE,
		Blocks.BLUE_TERRACOTTA to DyeColor.BLUE,
		Blocks.BROWN_TERRACOTTA to DyeColor.BROWN,
		Blocks.GREEN_TERRACOTTA to DyeColor.GREEN,
		Blocks.RED_TERRACOTTA to DyeColor.RED,
		Blocks.BLACK_TERRACOTTA to DyeColor.BLACK
	)
	private val tulips = mapOf(
		Blocks.ORANGE_TULIP to DyeColor.ORANGE,
		Blocks.PINK_TULIP to DyeColor.PINK,
		Blocks.RED_TULIP to DyeColor.RED,
		Blocks.WHITE_TULIP to DyeColor.WHITE
	)
	private val wools = mapOf(
		Blocks.WHITE_WOOL to DyeColor.WHITE,
		Blocks.ORANGE_WOOL to DyeColor.ORANGE,
		Blocks.MAGENTA_WOOL to DyeColor.MAGENTA,
		Blocks.LIGHT_BLUE_WOOL to DyeColor.LIGHT_BLUE,
		Blocks.YELLOW_WOOL to DyeColor.YELLOW,
		Blocks.LIME_WOOL to DyeColor.LIME,
		Blocks.PINK_WOOL to DyeColor.PINK,
		Blocks.GRAY_WOOL to DyeColor.GRAY,
		Blocks.LIGHT_GRAY_WOOL to DyeColor.LIGHT_GRAY,
		Blocks.CYAN_WOOL to DyeColor.CYAN,
		Blocks.PURPLE_WOOL to DyeColor.PURPLE,
		Blocks.BLUE_WOOL to DyeColor.BLUE,
		Blocks.BROWN_WOOL to DyeColor.BROWN,
		Blocks.GREEN_WOOL to DyeColor.GREEN,
		Blocks.RED_WOOL to DyeColor.RED,
		Blocks.BLACK_WOOL to DyeColor.BLACK
	)

	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		return listOf(when (args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				ctx.assertEntityInRange(entity)
				processEntity(entity)
			}
			is Vec3Iota -> {
				val position = args.getBlockPos(0, argc)
				ctx.assertVecInRange(position)
				processVec3d(position, ctx.world)
			}
			else -> NullIota()
		})
	}

	private fun processEntity(entity: Entity): Iota {
		return when (entity) {
			is ItemEntity -> {
				when (val item = entity.stack.item) {
					is BlockItem -> getDyeFromBlock(item.block)
					is DyeItem -> DyeIota(item.color)
					else -> NullIota()
				}
			}
			is SheepEntity -> DyeIota(entity.color)
			else -> NullIota()
		}
	}
	private fun processVec3d(position: BlockPos, world: ServerWorld): Iota {
		val state = world.getBlockState(position)
		if (state.block is SignBlock)
			return DyeIota((world.getBlockEntity(position) as SignBlockEntity).textColor)
		return getDyeFromBlock(world.getBlockState(position).block)
	}
	private fun getDyeFromBlock(block: Block): Iota {
		val entry = block.registryEntry
		if (entry.isIn(BlockTags.BEDS))
			return DyeIota(beds[block]!!)
		if (entry.isIn(BlockTags.CANDLE_CAKES))
			return DyeIota(cakeCandles[block]!!)
		if (entry.isIn(BlockTags.CANDLES))
			return DyeIota(candles[block]!!)
		if (entry.isIn(HexicalTags.CONCRETES))
			return DyeIota(concretes[block]!!)
		if (entry.isIn(HexicalTags.CONCRETE_POWDERS))
			return DyeIota(concretePowders[block]!!)
		if (entry.isIn(HexicalTags.GLAZED_TERRACOTTA))
			return DyeIota(glazedTerracotta[block]!!)
		if (entry.isIn(BlockTags.SHULKER_BOXES))
			return DyeIota(shulkerBoxes[block]!!)
		if (entry.isIn(HexicalTags.STAINED_GLASS))
			return DyeIota(stainedGlass[block]!!)
		if (entry.isIn(HexicalTags.STAINED_GLASS_PANES))
			return DyeIota(stainedGlassPanes[block]!!)
		if (entry.isIn(HexicalTags.TERRACOTTA))
			return DyeIota(terracotta[block]!!)
		if (entry.isIn(HexicalTags.TULIPS))
			return DyeIota(tulips[block]!!)
		if (entry.isIn(BlockTags.WOOL_CARPETS))
			return DyeIota(carpets[block]!!)
		if (entry.isIn(BlockTags.WOOL))
			return DyeIota(wools[block]!!)
		return NullIota()
	}
}