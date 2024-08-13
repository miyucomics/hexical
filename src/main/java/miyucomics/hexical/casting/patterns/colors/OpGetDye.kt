package miyucomics.hexical.casting.patterns.colors

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getBlockPos
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.EntityIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import miyucomics.hexical.casting.iota.DyeIota
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
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

class OpGetDye : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext) = listOf(
		when (args[0]) {
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
		if (state.block is SignBlock)
			return DyeIota((world.getBlockEntity(position) as SignBlockEntity).textColor.getName())
		return getDyeFromBlock(world.getBlockState(position).block)
	}

	private fun getDyeFromBlock(block: Block): Iota {
		val dye = DyeData.getDye(block) ?: return NullIota()
		return DyeIota(dye)
	}
}