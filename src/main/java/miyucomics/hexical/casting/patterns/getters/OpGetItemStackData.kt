package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.getItemEntity
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.item.ItemStack

class OpGetItemStackData(private val process: (ItemStack) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingEnvironment) = process(args.getItemStack(0, argc))
}

fun List<Iota>.getItemStack(idx: Int, argc: Int = 0): ItemStack = getItemEntity(idx, argc).stack