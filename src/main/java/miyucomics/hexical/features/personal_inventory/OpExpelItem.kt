package miyucomics.hexical.features.personal_inventory

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.getPositiveIntUnder
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

object OpExpelItem : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val caster = env.castingEntity as? ServerPlayerEntity ?: throw MishapBadCaster()
		val slot = args.getPositiveIntUnder(0, caster.inventory.size(), argc)
		return SpellAction.Result(Spell(slot), 0, listOf())
	}

	private data class Spell(val slot: Int) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {}
		override fun cast(env: CastingEnvironment, image: CastingImage): CastingImage {
			val caster = env.castingEntity as ServerPlayerEntity
			val target = caster.inventory.getStack(slot)
			if (target.isEmpty)
				return image.copy(stack = image.stack.toList().plus(NullIota()))

			val copied = target.copy()
			caster.inventory.setStack(slot, ItemStack.EMPTY)

			val itemEntity: ItemEntity? = caster.dropItem(copied, false, true)
			if (itemEntity == null) {
				caster.inventory.setStack(slot, target)
				return image.copy(stack = image.stack.toList().plus(NullIota()))
			}

			caster.currentScreenHandler.sendContentUpdates()
			return image.copy(stack = image.stack.toList().plus(EntityIota(itemEntity)))
		}
	}
}