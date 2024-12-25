package miyucomics.hexical.casting.patterns.wristpocket

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getItemEntity
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.state.PersistentStateHandler
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d

class OpSleight : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env.castingEntity !is ServerPlayerEntity)
			throw MishapBadCaster()

		when (args[0]) {
			is EntityIota -> {
				val item = args.getItemEntity(0, argc)
				env.assertEntityInRange(item)
				return SpellAction.Result(SwapSpell(item), MediaConstants.DUST_UNIT / 4, listOf())
			}
			is Vec3Iota -> {
				val position = args.getVec3(0, argc)
				env.assertVecInRange(position)
				return SpellAction.Result(ConjureSpell(position), MediaConstants.DUST_UNIT / 4, listOf())
			}
			else -> throw MishapInvalidIota.of(args[0], 0, "entity_or_vector")
		}
	}

	private data class ConjureSpell(val position: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val wristpocketed = PersistentStateHandler.getWristpocketStack(env.castingEntity as ServerPlayerEntity)
			if (wristpocketed != ItemStack.EMPTY && wristpocketed.item != Items.AIR)
				env.world.spawnEntity(ItemEntity(env.world, position.x, position.y, position.z, PersistentStateHandler.getWristpocketStack(env.castingEntity as ServerPlayerEntity)))
			PersistentStateHandler.setWristpocketStack(env.castingEntity as ServerPlayerEntity, ItemStack.EMPTY)
		}
	}

	private data class SwapSpell(val item: ItemEntity) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val wristpocketed = PersistentStateHandler.getWristpocketStack(env.castingEntity as ServerPlayerEntity)
			PersistentStateHandler.setWristpocketStack(env.castingEntity as ServerPlayerEntity, item.stack)
			if (wristpocketed != ItemStack.EMPTY && wristpocketed.item != Items.AIR)
				item.stack = wristpocketed
			else
				item.discard()
		}
	}
}