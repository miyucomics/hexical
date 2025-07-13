package miyucomics.hexical.casting.actions.lore

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getItemEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import miyucomics.hexpose.iotas.getText
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

object OpItemName : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val item = args.getItemEntity(0, argc)
		env.assertEntityInRange(item)
		if (args[1] is NullIota)
			return SpellAction.Result(Spell(item.stack, null), 0, listOf(ParticleSpray.cloud(item.pos, 1.0)))
		val title = args.getText(1, argc)
		return SpellAction.Result(Spell(item.stack, title), 0, listOf(ParticleSpray.cloud(item.pos, 1.0)))
	}

	private data class Spell(val stack: ItemStack, val lore: Text?) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			stack.setCustomName(lore)
		}
	}
}