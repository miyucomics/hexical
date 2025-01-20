package miyucomics.hexical.casting.patterns.colors

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.*
import miyucomics.hexical.casting.iota.DyeIota
import miyucomics.hexical.casting.iota.IdentifierIota
import miyucomics.hexical.casting.iota.getIdentifier
import miyucomics.hexical.data.DyeData
import net.minecraft.block.Block
import net.minecraft.block.SignBlock
import net.minecraft.block.entity.SignBlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.mob.ShulkerEntity
import net.minecraft.entity.passive.CatEntity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.item.BlockItem
import net.minecraft.registry.Registries
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

class OpGetDye : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment) = listOf(
		when (args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				env.assertEntityInRange(entity)
				processEntity(entity)
			}
			is IdentifierIota -> {
				when (val item = Registries.ITEM.get(args.getIdentifier(0, argc))) {
					is BlockItem -> getDyeFromBlock(item.block)
					else -> {
						if (DyeData.getDye(item) != null)
							DyeIota(DyeData.getDye(item)!!)
						else
							NullIota()
					}
				}
			}
			is Vec3Iota -> {
				val position = args.getBlockPos(0, argc)
				env.assertPosInRange(position)
				processVec3d(position, env.world)
			}
			else -> NullIota()
		}
	)

	private fun processEntity(entity: Entity): Iota {
		return when (entity) {
			is CatEntity -> DyeIota(entity.collarColor.getName())
			is ItemEntity -> {
				when (val item = entity.stack.item) {
					is BlockItem -> getDyeFromBlock(item.block)
					else -> {
						if (DyeData.getDye(item) != null)
							DyeIota(DyeData.getDye(item)!!)
						else
							NullIota()
					}
				}
			}
			is SheepEntity -> DyeIota(entity.color.getName())
			is ShulkerEntity -> {
				if (entity.color == null)
					NullIota()
				else
					DyeIota(entity.color!!.getName())
			}
			is WolfEntity -> DyeIota(entity.collarColor.getName())
			else -> NullIota()
		}
	}

	private fun processVec3d(position: BlockPos, world: ServerWorld): Iota {
		val state = world.getBlockState(position)
		if (state.block is SignBlock) {
			val sign = world.getBlockEntity(position) as SignBlockEntity
			return ListIota(listOf(DyeIota(sign.frontText.color.getName()), DyeIota(sign.backText.color.getName())))
		}
		return getDyeFromBlock(world.getBlockState(position).block)
	}

	private fun getDyeFromBlock(block: Block): Iota {
		val dye = DyeData.getDye(block) ?: return NullIota()
		return DyeIota(dye)
	}
}