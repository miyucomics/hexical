package miyucomics.hexical.casting.patterns.scrying

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getItemEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.item.ItemStack

class OpGetItemStackData(private val process: (ItemStack) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val item = args.getItemEntity(0, argc)
		env.assertEntityInRange(item)
		return process(item.stack)
	}
}