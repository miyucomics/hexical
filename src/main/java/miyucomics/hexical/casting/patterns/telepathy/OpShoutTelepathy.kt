package miyucomics.hexical.casting.patterns.telepathy

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.network.packet.s2c.play.TitleS2CPacket

class OpShoutTelepathy : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		ctx.caster.networkHandler.sendPacket(TitleS2CPacket(args[0].display()))
		return listOf()
	}
}