package miyucomics.hexical.features.personal_inventory

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.getItemEntity
import at.petrak.hexcasting.api.casting.getPositiveIntUnder
import at.petrak.hexcasting.api.casting.iota.BooleanIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import kotlin.math.min

object OpAbsorbItem : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val caster = env.castingEntity as? ServerPlayerEntity ?: throw MishapBadCaster()
		val target = args.getItemEntity(0, argc)
		env.assertEntityInRange(target)
		val slot = args.getPositiveIntUnder(1, caster.inventory.size(), argc)
		return SpellAction.Result(Spell(target, slot), 0, listOf())
	}

	private data class Spell(val target: ItemEntity, val slot: Int) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {}
		override fun cast(env: CastingEnvironment, image: CastingImage): CastingImage {
			val result = pullEntityIntoSlot(env.castingEntity as ServerPlayerEntity, target, slot)
			return image.copy(stack = image.stack.toList().plus(BooleanIota(result)))
		}
	}

	private fun pullEntityIntoSlot(player: ServerPlayerEntity, target: ItemEntity, slot: Int): Boolean {
		val intruder = target.stack
		if (intruder.isEmpty)
			return false

		val existing = player.inventory.getStack(slot)
		when {
			existing.isEmpty -> {
				player.inventory.setStack(slot, intruder.copy())
				target.discard()
				player.currentScreenHandler.sendContentUpdates()
				return true
			}
			ItemStack.canCombine(existing, intruder) -> {
				val maxCount = min(existing.maxCount, player.inventory.maxCountPerStack)
				val remainingSpace = maxCount - existing.count

				if (remainingSpace > 0) {
					val toMove = min(remainingSpace, intruder.count)

					existing.increment(toMove)
					intruder.decrement(toMove)

					if (intruder.isEmpty)
						target.discard()
					else
						target.stack = intruder

					player.currentScreenHandler.sendContentUpdates()
					return true
				}
			}
		}

		return false
	}
}