package miyucomics.hexical.casting.patterns.scrying

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getItemEntity
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

class OpGetMaxMedia : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val entity = args.getItemEntity(0, argc)
		val holder = IXplatAbstractions.INSTANCE.findMediaHolder(entity.stack) ?: return listOf(NullIota())
		return listOf(DoubleIota(holder.maxMedia.toDouble() / MediaConstants.DUST_UNIT.toDouble()))
	}
}