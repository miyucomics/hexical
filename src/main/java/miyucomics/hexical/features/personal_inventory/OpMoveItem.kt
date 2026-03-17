package miyucomics.hexical.features.personal_inventory

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.getPositiveIntUnder
import at.petrak.hexcasting.api.casting.iota.BooleanIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import kotlin.math.min

object OpMoveItem : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val caster = env.castingEntity as? ServerPlayerEntity ?: throw MishapBadCaster()
		val a = args.getPositiveIntUnder(0, caster.inventory.size(), argc)
		val b = args.getPositiveIntUnder(1, caster.inventory.size(), argc)
		return SpellAction.Result(Spell(a, b), 0, listOf())
	}

	private data class Spell(val a: Int, val b: Int) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {}
		override fun cast(env: CastingEnvironment, image: CastingImage): CastingImage {
			val result = swapItem(env.castingEntity as ServerPlayerEntity, a, b)
			return image.copy(stack = image.stack.toList().plus(BooleanIota(result)))
		}
	}

	private fun swapItem(player: ServerPlayerEntity, a: Int, b: Int): Boolean {
		val from = player.inventory.getStack(a)
		val to = player.inventory.getStack(b)

		when {
			to.isEmpty -> {
				player.inventory.setStack(a, ItemStack.EMPTY);
				player.inventory.setStack(b, from);
				return true
			}
			ItemStack.canCombine(from, to) -> {
				val maxCount = min(to.maxCount, player.inventory.maxCountPerStack)
				val remainingSpace = maxCount - to.count

				if (remainingSpace > 0) {
					val toMove = min(remainingSpace, from.count)
					to.increment(toMove)
					from.decrement(toMove)
					player.currentScreenHandler.sendContentUpdates()
					return true
				}
			}
		}

		return false
	}
}