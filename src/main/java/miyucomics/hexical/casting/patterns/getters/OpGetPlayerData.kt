package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getPlayer
import at.petrak.hexcasting.api.spell.iota.Iota
import net.minecraft.server.network.ServerPlayerEntity

class OpGetPlayerData(private val process: (ServerPlayerEntity) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext) = process(args.getPlayer(0, argc))
}