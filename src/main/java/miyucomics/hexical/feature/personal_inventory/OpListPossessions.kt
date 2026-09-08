package miyucomics.hexical.feature.personal_inventory

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import net.minecraft.server.network.ServerPlayerEntity
import ram.talia.moreiotas.api.casting.iota.ItemStackIota

object OpListPossessions : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val caster = env.castingEntity as? ServerPlayerEntity ?: throw MishapBadCaster()
		return (0 until caster.inventory.size()).map { ItemStackIota.createFiltered(caster.inventory.getStack(it)) }.asActionResult
	}
}