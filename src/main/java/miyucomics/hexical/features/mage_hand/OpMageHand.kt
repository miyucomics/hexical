package miyucomics.hexical.features.mage_hand

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.features.wristpocket.WristpocketUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

object OpMageHand : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val heldItem = when (val tool = args[1]) {
			is EntityIota if tool.entity is ItemEntity -> {
				env.assertEntityInRange(tool.entity)
				(tool.entity as ItemEntity).stack
			}
			is NullIota -> ItemStack.EMPTY
			else -> throw MishapInvalidIota.of(tool, 0, "item_entity_or_null")
		}

		when (val iota = args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				env.assertEntityInRange(entity)
				return SpellAction.Result(EntitySpell(entity, heldItem), MediaConstants.DUST_UNIT, listOf())
			}
			is Vec3Iota -> {
				val position = args.getBlockPos(0, argc)
				env.assertPosInRange(position)
				return SpellAction.Result(BlockSpell(position, heldItem), MediaConstants.DUST_UNIT, listOf())
			}
			else -> throw MishapInvalidIota.of(iota, 1, "entity_or_vector")
		}
	}

	private data class BlockSpell(val position: BlockPos, val item: ItemStack) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val resultantStack = FakePlayerUtils.useItemAt(env.world, item, env.castingEntity as? ServerPlayerEntity, position)
			WristpocketUtils.setWristpocketStack(env, resultantStack)
		}
	}

	private data class EntitySpell(val entity: Entity, val item: ItemStack) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val resultantStack = FakePlayerUtils.useItemOnEntity(env.world, item, env.castingEntity as? ServerPlayerEntity, entity)
			WristpocketUtils.setWristpocketStack(env, resultantStack)
		}
	}
}