package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.casting.mishaps.NoStaffMishap
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand

class OpAutograph : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env !is StaffCastEnv)
			throw NoStaffMishap()
		return SpellAction.Result(Spell(env.otherHand), 0, listOf())
	}

	private data class Spell(val hand: Hand) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val caster = env.castingEntity
			if (caster !is ServerPlayerEntity)
				return

			val item = caster.getStackInHand(hand)
			if (!item.orCreateNbt.contains("autographs"))
				item.orCreateNbt.put("autographs", NbtList())

			val list = item.orCreateNbt.getList("autographs", NbtCompound.COMPOUND_TYPE.toInt())
			val compound = NbtCompound()
			compound.putString("name", caster.entityName)
			compound.putCompound("pigment", IXplatAbstractions.INSTANCE.getPigment(caster).serializeToNBT())
			list.add(compound)
		}
	}
}