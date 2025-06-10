package miyucomics.hexical.casting.patterns.autograph

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPlayer
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexposition.iotas.getItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement

class OpHasAutograph : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val stack = args.getItemStack(0, argc)
		if (!stack.hasNbt())
			return false.asActionResult
		if (!stack.nbt!!.contains("autographs"))
			return false.asActionResult
		val player = args.getPlayer(1, argc)
		val list = stack.nbt!!.getList("autographs", NbtElement.COMPOUND_TYPE.toInt())
		return (list.count { (it as NbtCompound).getString("name") == player.entityName } > 0).asActionResult
	}
}