package miyucomics.hexical.casting.patterns.getters.types

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingEnvironment
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.casting.iota.getIdentifier
import net.minecraft.block.Block
import net.minecraft.util.registry.Registry

class OpGetBlockTypeData(private val process: (Block) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingEnvironment): List<Iota> {
		val id = args.getIdentifier(0, argc)
		if (!Registry.BLOCK.containsId(id))
			throw MishapInvalidIota.of(args[0], 0, "block_id")
		return process(Registry.BLOCK.get(id))
	}
}