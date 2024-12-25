package miyucomics.hexical.casting.patterns.wristpocket

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.casting.mishaps.InedibleWristpocketMishap
import miyucomics.hexical.state.PersistentStateHandler
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity

class OpIngest : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env.castingEntity !is ServerPlayerEntity)
			throw MishapBadCaster()

		val stack = PersistentStateHandler.getWristpocketStack(env.caster)
		if (stack.isOf(Items.POTION) || stack.isOf(Items.HONEY_BOTTLE) || stack.isOf(Items.MILK_BUCKET) || stack.item.isFood)
			return SpellAction.Result(Spell(stack), MediaConstants.DUST_UNIT, listOf())
		throw InedibleWristpocketMishap()
	}

	private data class Spell(val stack: ItemStack) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val originalItem = env.caster.getStackInHand(env.castingHand)
			env.caster.setStackInHand(env.castingHand, stack)
			val newStack = stack.finishUsing(env.world, env.caster)
			PersistentStateHandler.setWristpocketStack(env.caster, newStack)
			env.caster.setStackInHand(env.castingHand, originalItem)
		}
	}
}