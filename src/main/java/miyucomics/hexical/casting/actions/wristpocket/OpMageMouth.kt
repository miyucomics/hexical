package miyucomics.hexical.casting.actions.wristpocket

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.casting.mishaps.InedibleWristpocketMishap
import miyucomics.hexical.features.player.fields.wristpocket
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity

class OpMageMouth : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env.castingEntity !is ServerPlayerEntity)
			throw MishapBadCaster()

		val wristpocket = (env.castingEntity as ServerPlayerEntity).wristpocket
		if (wristpocket.isOf(Items.POTION) || wristpocket.isOf(Items.HONEY_BOTTLE) || wristpocket.isOf(Items.MILK_BUCKET) || wristpocket.item.isFood)
			return SpellAction.Result(Spell(wristpocket), MediaConstants.DUST_UNIT, listOf())
		throw InedibleWristpocketMishap()
	}

	private data class Spell(val wristpocket: ItemStack) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val caster = env.castingEntity as ServerPlayerEntity
			val original = caster.getStackInHand(env.castingHand)
			caster.setStackInHand(env.castingHand, wristpocket)
			val newStack = wristpocket.finishUsing(env.world, caster)
			caster.wristpocket = newStack
			caster.setStackInHand(env.castingHand, original)
		}
	}
}