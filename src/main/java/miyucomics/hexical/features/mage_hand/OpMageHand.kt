package miyucomics.hexical.features.mage_hand

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getBool
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

object OpMageHand : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val toUse = when (val tool = args[1]) {
			is EntityIota if tool.entity is ItemEntity -> {
				env.assertEntityInRange(tool.entity)
				tool.entity as ItemEntity
			}
			is NullIota -> null
			else -> throw MishapInvalidIota.of(tool, 1, "item_entity_or_null")
		}

		when (val iota = args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				env.assertEntityInRange(entity)
				return SpellAction.Result(EntitySpell(entity, toUse, args.getBool(2, argc)), MediaConstants.DUST_UNIT, listOf())
			}
			is Vec3Iota -> {
				val position = args.getBlockPos(0, argc)
				env.assertPosInRange(position)
				return SpellAction.Result(BlockSpell(position, toUse, args.getBool(2, argc)), MediaConstants.DUST_UNIT, listOf())
			}
			else -> throw MishapInvalidIota.of(iota, 2, "entity_or_vector")
		}
	}

	private data class BlockSpell(val position: BlockPos, val item: ItemEntity?, val sneak: Boolean) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val tool = item?.stack ?: ItemStack.EMPTY
			val resultantStack = FakePlayerUtils.useItemAt(env.world, tool, env.castingEntity as? ServerPlayerEntity, position, sneak)
			item?.stack = resultantStack
		}
	}

	private data class EntitySpell(val entity: Entity, val item: ItemEntity?, val sneak: Boolean) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val tool = item?.stack ?: ItemStack.EMPTY
			val resultantStack = FakePlayerUtils.useItemOnEntity(env.world, tool, env.castingEntity as? ServerPlayerEntity, entity, sneak)
			item?.stack = resultantStack
		}
	}
}