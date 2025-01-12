package miyucomics.hexical.casting.patterns.scrying

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.*
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.mod.HexConfig
import at.petrak.hexcasting.api.utils.extractMedia
import at.petrak.hexcasting.api.utils.scanPlayerForMediaStuff
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.passive.AllayEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.MathHelper
import kotlin.math.max
import kotlin.math.min

class OpGetMedia : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		return listOf(when (args[0]) {
			is EntityIota -> {
				val entity = args.getEntity(0, argc)
				env.assertEntityInRange(entity)
				when (entity) {
					is AllayEntity -> DoubleIota(100.0)
					is ItemEntity -> {
						val holder = IXplatAbstractions.INSTANCE.findMediaHolder(entity.stack)
						if (holder == null)
							NullIota()
						else
							DoubleIota(holder.media.toDouble() / MediaConstants.DUST_UNIT.toDouble())
					}
					is ServerPlayerEntity -> {
						val sources = scanPlayerForMediaStuff(entity)
						var costLeft = Long.MAX_VALUE

						for (source in sources) {
							val found = extractMedia(source, costLeft, drainForBatteries = false, simulate = true)
							costLeft -= found
							if (costLeft <= 0)
								break
						}

						if (costLeft > 0) {
							val mediaToHealth = HexConfig.common().mediaToHealthRate()
							val healthToRemove = max(costLeft / mediaToHealth, 0.5)
							val simulatedRemovedMedia = MathHelper.ceil(min(entity.health.toDouble(), healthToRemove) * mediaToHealth).toLong()
							costLeft -= simulatedRemovedMedia
						}

						DoubleIota((Long.MAX_VALUE - costLeft).toDouble() / MediaConstants.DUST_UNIT.toDouble())
					}
					else -> NullIota()
				}
			}
			is Vec3Iota -> {
				val position = args.getBlockPos(0, argc)
				env.assertPosInRange(position)
				val blockEntity = env.world.getBlockEntity(position)
				if (blockEntity == null || blockEntity !is BlockEntityAbstractImpetus)
					NullIota()
				else
					DoubleIota(blockEntity.media.toDouble() / MediaConstants.DUST_UNIT.toDouble())
			}
			else -> throw MishapInvalidIota.of(args[0], 0, "entity_or_vector")
		})
	}
}