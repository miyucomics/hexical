package miyucomics.hexical.casting.patterns.wristpocket

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.casting.mishaps.InedibleWristpocketMishap
import miyucomics.hexical.interfaces.PlayerEntityMinterface
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity

class OpMageMouth : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env.castingEntity !is ServerPlayerEntity)
			throw MishapBadCaster()

		val stack = (env.castingEntity as PlayerEntityMinterface).getWristpocket()
		if (stack.isOf(Items.POTION) || stack.isOf(Items.HONEY_BOTTLE) || stack.isOf(Items.MILK_BUCKET) || stack.item.isFood)
			return SpellAction.Result(Spell(stack), MediaConstants.DUST_UNIT, listOf())
		throw InedibleWristpocketMishap()
	}

	private data class Spell(val stack: ItemStack) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val caster = env.castingEntity as ServerPlayerEntity
			val originalItem = caster.getStackInHand(env.castingHand)
			caster.setStackInHand(env.castingHand, stack)
			val newStack = stack.finishUsing(env.world, caster)
			(caster as PlayerEntityMinterface).setWristpocket(newStack)
			caster.setStackInHand(env.castingHand, originalItem)
		}
	}
}