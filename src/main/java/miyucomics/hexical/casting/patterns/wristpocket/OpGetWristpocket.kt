package miyucomics.hexical.casting.patterns.wristpocket

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.state.PersistentStateHandler
import net.minecraft.item.ItemStack

class OpGetWristpocket(private val process: (ItemStack) -> List<Iota>) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingEnvironment) = process(PersistentStateHandler.getWristpocketStack(ctx.caster))
}