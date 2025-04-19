package miyucomics.hexical.casting.patterns.autograph

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.utils.getOrCreateList
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.casting.mishaps.NoStaffMishap
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity

class OpAutograph : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env.castingEntity !is PlayerEntity)
			throw MishapBadCaster()
		if (env !is StaffCastEnv)
			throw NoStaffMishap()
		val stack = env.getHeldItemToOperateOn { true }
		if (stack == null)
			throw MishapBadOffhandItem.of(null, "anything")
		return SpellAction.Result(Spell(stack.stack), 0, listOf())
	}

	private data class Spell(val stack: ItemStack) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val caster = env.castingEntity as ServerPlayerEntity
			val list = stack.orCreateNbt.getOrCreateList("autographs", NbtCompound.COMPOUND_TYPE.toInt())
			list.removeIf { compound -> (compound as NbtCompound).getString("name") == caster.entityName }

			val compound = NbtCompound()
			compound.putString("name", caster.entityName)
			compound.putCompound("pigment", IXplatAbstractions.INSTANCE.getPigment(caster).serializeToNBT())
			list.addFirst(compound)
		}
	}
}