package miyucomics.hexical.feature.dyeing.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexical.feature.dyeing.DyeIota
import miyucomics.hexical.feature.dyeing.DyeOption
import miyucomics.hexical.feature.dyeing.DyeingEntityRegistry
import miyucomics.hexical.feature.dyeing.DyeingUtils
import miyucomics.hexpose.iotas.getItemType
import ram.talia.moreiotas.api.casting.iota.ItemTypeIota

object OpGetDye : ConstMediaAction {
	override val argc = 1

	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> = listOf(
		when (args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				env.assertEntityInRange(entity)
				wrap(DyeingEntityRegistry.getColor(entity))
			}
			is ItemTypeIota -> wrap(DyeingUtils.getDye(args.getItemType(0, argc)))
			is Vec3Iota -> {
				val pos = args.getBlockPos(0, argc)
				env.assertPosInRange(pos)
				wrap(DyeingUtils.getDye(env.world.getBlockState(pos).block))
			}
			else -> throw MishapInvalidIota.of(args[0], 0, "containing_dye")
		}
	)

	private fun wrap(dye: DyeOption?): Iota = dye?.let(::DyeIota) ?: NullIota()
}