package miyucomics.hexical.casting.operators.telepathy

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.network.packet.s2c.play.TitleS2CPacket

class OpShoutTelepathy : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		ctx.caster.networkHandler.sendPacket(TitleS2CPacket(args[0].display()))
		return listOf()
	}
}