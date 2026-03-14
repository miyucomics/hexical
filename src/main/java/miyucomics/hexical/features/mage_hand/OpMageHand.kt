package miyucomics.hexical.features.mage_hand

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBool
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d

object OpMageHand : SpellAction {
	override val argc = 4
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val toUse = when (val tool = args[0]) {
			is EntityIota if tool.entity is ItemEntity -> {
				env.assertEntityInRange(tool.entity)
				tool.entity as ItemEntity
			}
			is NullIota -> null
			else -> throw MishapInvalidIota.of(tool, 1, "item_entity_or_null")
		}

		val position = args.getVec3(1, argc)
		env.assertVecInRange(position)
		val rotation = args.getVec3(2, argc).normalize()

		return SpellAction.Result(Spell(toUse, position, rotation, args.getBool(3, argc)), MediaConstants.DUST_UNIT, listOf())
	}

	private data class Spell(val toUse: ItemEntity?, val position: Vec3d, val rotation: Vec3d, val sneak: Boolean) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val tool = toUse?.stack ?: ItemStack.EMPTY
			val dispense = toUse?.pos ?: position
			FakeInteractionUtils.rightClickAt(env.world, tool, env.castingEntity as? ServerPlayerEntity, position, rotation, sneak = sneak).forEach { env.world.spawnEntity(ItemEntity(env.world, dispense.x, dispense.y, dispense.z, it, 0.0, 0.0, 0.0)) }
			toUse?.discard()
		}
	}
}