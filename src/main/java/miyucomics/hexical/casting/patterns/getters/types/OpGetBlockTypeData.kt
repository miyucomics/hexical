package miyucomics.hexical.casting.patterns.getters.types

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.asActionResult
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.casting.iota.getIdentifier
import net.minecraft.block.CropBlock
import net.minecraft.util.registry.Registry

class OpGetBlockTypeData(private val mode: Int) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val id = args.getIdentifier(0, argc)
		if (!Registry.BLOCK.containsId(id))
			throw MishapInvalidIota.of(args[0], 0, "block_id")
		val block = Registry.BLOCK.get(args.getIdentifier(0, argc))
		return when (mode) {
			0 -> block.hardness.asActionResult
			1 -> block.blastResistance.asActionResult
			else -> throw IllegalStateException()
		}
	}
}