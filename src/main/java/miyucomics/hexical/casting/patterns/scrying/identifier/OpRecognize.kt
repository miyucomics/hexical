package miyucomics.hexical.casting.patterns.scrying.identifier

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexical.casting.iota.asActionResult
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.decoration.ItemFrameEntity
import net.minecraft.registry.Registries

class OpRecognize : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		return when (val arg = args[0]) {
			is EntityIota -> {
				when (arg.entity) {
					is ItemEntity -> Registries.ITEM.getId((arg.entity as ItemEntity).stack.item).asActionResult()
					is ItemFrameEntity -> Registries.ITEM.getId((arg.entity as ItemFrameEntity).heldItemStack.item).asActionResult()
					else -> throw MishapInvalidIota.of(arg, 0, "recognizable")
				}
			}
			else -> throw MishapInvalidIota.of(arg, 0, "recognizable")
		}
	}
}