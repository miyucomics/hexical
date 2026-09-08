package miyucomics.hexical.misc

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage.ParenthesizedIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapOthersName
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand

object CastingUtils {
	fun assertNoTruename(iota: Iota, env: CastingEnvironment) {
		val truename = MishapOthersName.getTrueNameFromDatum(iota, env.castingEntity as? ServerPlayerEntity)
		if (truename != null)
			throw MishapOthersName(truename)
	}

	fun isEnlightened(player: ServerPlayerEntity): Boolean {
		val advancement = player.getServer()!!.advancementLoader[HexAPI.modLoc("enlightenment")]
		val tracker = player.advancementTracker
		return tracker.getProgress(advancement) != null && tracker.getProgress(advancement).isDone
	}

	@JvmStatic
	fun giveIota(player: ServerPlayerEntity, iota: Iota) {
		val image = IXplatAbstractions.INSTANCE.getStaffcastVM(player, Hand.MAIN_HAND).image
		val newImage = if (image.parenCount == 0) {
			val stack = image.stack.toMutableList()
			stack.add(iota)
			image.copy(stack = stack)
		} else {
			val parenthesized = image.parenthesized.toMutableList()
			parenthesized.add(ParenthesizedIota(iota, false))
			image.copy(parenthesized = parenthesized)
		}
		IXplatAbstractions.INSTANCE.setStaffcastImage(player, newImage)
	}


	fun getHex(nbt: NbtCompound, world: ServerWorld) = HexSerialization.deserializeHex(nbt.getList("hex", NbtElement.COMPOUND_TYPE.toInt()), world)
	fun getMedia(nbt: NbtCompound) = nbt.getLong("media")
	fun getMaxMedia(nbt: NbtCompound) = nbt.getLong("max_media")
}