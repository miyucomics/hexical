package miyucomics.hexical.casting.patterns.wristpocket

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import miyucomics.hexical.casting.iota.asActionResult
import miyucomics.hexical.state.PersistentStateHandler
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.registry.Registry

class OpGetWristpocket(private val mode: Int) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val wristpocket = PersistentStateHandler.wristpocketItem(ctx.caster)
		if (wristpocket.isOf(Items.AIR) || wristpocket == ItemStack.EMPTY)
			return when (mode) {
				0 -> listOf(NullIota())
				1 -> (0).asActionResult
				else -> throw IllegalStateException()
			}
		return when (mode) {
			0 -> Registry.ITEM.getId(wristpocket.item).asActionResult()
			1 -> wristpocket.count.asActionResult
			else -> throw IllegalStateException()
		}
	}
}