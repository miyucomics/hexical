package miyucomics.hexical.features.dyes

import at.petrak.hexcasting.api.casting.ParticleSpray
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
import miyucomics.hexical.features.specklikes.Specklike
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.mob.ShulkerEntity
import net.minecraft.entity.passive.CatEntity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.state.property.Property
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.util.*

object OpDye : SpellAction {
	override val argc = 2
	private const val COST = MediaConstants.DUST_UNIT / 8
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val dye = args.getDye(1, argc)
		when (args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				env.assertEntityInRange(entity)
				return when (entity) {
					is CatEntity -> {
						val trueDye = args.getColoredDye(1, argc)
						SpellAction.Result(CatSpell(entity, trueDye), COST, listOf(ParticleSpray.cloud(entity.pos, 1.0)))
					}
					is SheepEntity -> {
						val trueDye = args.getColoredDye(1, argc)
						SpellAction.Result(SheepSpell(entity, trueDye), COST, listOf(ParticleSpray.cloud(entity.pos, 1.0)))
					}
					is ShulkerEntity -> {
						val trueDye = args.getColoredDye(1, argc)
						SpellAction.Result(ShulkerSpell(entity, trueDye), COST, listOf(ParticleSpray.cloud(entity.pos, 1.0)))
					}
					is Specklike -> {
						val trueDye = args.getColoredDye(1, argc)
						SpellAction.Result(SpecklikeSpell(entity, trueDye), COST, listOf(ParticleSpray.cloud(entity.pos, 1.0)))
					}
					is WolfEntity -> {
						val trueDye = args.getColoredDye(1, argc)
						SpellAction.Result(WolfSpell(entity, trueDye), COST, listOf(ParticleSpray.cloud(entity.pos, 1.0)))
					}
					is ItemEntity -> {
						when (val item = entity.stack.item) {
							is BlockItem -> {
								if (DyeDataHook.isDyeable(item.block))
									SpellAction.Result(BlockItemSpell(entity, item.block, dye), COST, listOf(ParticleSpray.cloud(entity.pos, 1.0)))
								else
									throw DyeableMishap(entity.pos)
							}
							else -> {
								if (DyeDataHook.isDyeable(item))
									SpellAction.Result(ItemSpell(entity, item, dye), COST, listOf(ParticleSpray.cloud(entity.pos, 1.0)))
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
				if (!DyeDataHook.isDyeable(state.block))
					throw DyeableMishap(position.toCenterPos())
				return SpellAction.Result(BlockSpell(position, state, dye), COST, listOf(ParticleSpray.cloud(Vec3d.ofCenter(position), 1.0)))
			}
			else -> throw MishapInvalidIota.of(args[0], 1, "entity_or_vector")
		}
	}

	private data class BlockSpell(val position: BlockPos, val state: BlockState, val dye: DyeOption) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			var newState = DyeDataHook.getNewBlock(state.block, dye)!!
			state.properties.filter(newState.properties::contains).forEach { property ->
				@Suppress("UNCHECKED_CAST")
				val typedProperty: Property<Comparable<Any>> = property as Property<Comparable<Any>>
				newState = newState.with(typedProperty, state.get(typedProperty))
			}
			env.world.setBlockState(position, newState)
		}
	}

	private data class BlockItemSpell(val item: ItemEntity, val block: Block, val dye: DyeOption) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val newStack = ItemStack(DyeDataHook.getNewBlock(block, dye)!!.block.asItem(), item.stack.count)
			newStack.nbt = item.stack.nbt
			item.stack = newStack
		}
	}

	private data class CatSpell(val cat: CatEntity, val dye: DyeColor) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			cat.collarColor = dye
		}
	}

	private data class ItemSpell(val entity: ItemEntity, val item: Item, val dye: DyeOption) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val newStack = ItemStack(DyeDataHook.getNewItem(item, dye), entity.stack.count)
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