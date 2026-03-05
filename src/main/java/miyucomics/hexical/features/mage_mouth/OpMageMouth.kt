package miyucomics.hexical.features.mage_mouth

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getItemEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.inits.HexicalItems
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity

object OpMageMouth : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env.castingEntity !is ServerPlayerEntity)
			throw MishapBadCaster()

		val itemEntity = args.getItemEntity(0, argc)
		val attemptToEat = itemEntity.stack
		if ((attemptToEat.isOf(Items.POTION) || attemptToEat.isOf(Items.HONEY_BOTTLE) || attemptToEat.isOf(Items.MILK_BUCKET) || attemptToEat.item.isFood) && !attemptToEat.isIn(HexicalItems.CAN_NOT_MAGE_MOUTH_TAG))
			return SpellAction.Result(Spell(itemEntity, attemptToEat), MediaConstants.DUST_UNIT, listOf())
		throw InedibleItemMishap()
	}

	private data class Spell(val itemEntity: ItemEntity, val toEat: ItemStack) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val caster = env.castingEntity as ServerPlayerEntity
			val originalStack = caster.getStackInHand(env.castingHand)
			caster.setStackInHand(env.castingHand, toEat)
			val leftover = toEat.finishUsing(env.world, caster)
			itemEntity.stack = leftover
			caster.setStackInHand(env.castingHand, originalStack)
		}
	}
}