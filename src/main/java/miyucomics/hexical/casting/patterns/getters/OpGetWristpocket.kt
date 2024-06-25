package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import miyucomics.hexical.casting.iota.IdentifierIota
import miyucomics.hexical.state.PersistentStateHandler
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.registry.Registry
import java.lang.IllegalStateException

class OpGetWristpocket(private val mode: Int) : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val wristpocket = PersistentStateHandler.wristpocketItem(ctx.caster)
		if (wristpocket.isOf(Items.AIR) || wristpocket == ItemStack.EMPTY)
			return listOf(when (mode) {
				0 -> NullIota()
				1 -> DoubleIota(0.0)
				else -> throw IllegalStateException()
			})
		return listOf(when (mode) {
			0 -> IdentifierIota(Registry.ITEM.getId(wristpocket.item))
			1 -> DoubleIota(wristpocket.count.toDouble())
			else -> throw IllegalStateException()
		})
	}
}