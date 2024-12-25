package miyucomics.hexical.casting.patterns.staff

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.math.Vec3d

class OpConjureTchotchke : SpellAction {
	override val argc = 4
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getVec3(0, argc)
		env.assertVecInRange(position)
		val battery = args.getPositiveDoubleUnderInclusive(1, 200_000.0, argc)
		val rank = args.getInt(2, argc)
		if (rank <= 0)
			throw MishapInvalidIota.of(args[2], 2, "integer_natural")
		val instructions = args.getList(3, argc).toList()
		CastingUtils.assertNoTruename(args[3], env.caster)
		return SpellAction.Result(Spell(position, (battery * MediaConstants.DUST_UNIT).toInt(), rank, instructions), MediaConstants.SHARD_UNIT + MediaConstants.DUST_UNIT * battery.toInt(), listOf(ParticleSpray.burst(position, 1.0)))
	}

	private data class Spell(val position: Vec3d, val battery: Int, val rank: Int, val instructions: List<Iota>) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val stack = ItemStack(HexicalItems.CONJURED_STAFF_ITEM, 1)
			stack.orCreateNbt.putInt("rank", rank)
			val hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(stack)
			hexHolder?.writeHex(instructions, battery)
			val offhand = env.caster.getStackInHand(env.otherHand).item
			if (itemMap.containsKey(offhand))
				stack.orCreateNbt.putInt("sprite", itemMap[offhand]!!)
			env.world.spawnEntity(ItemEntity(env.world, position.x, position.y, position.z, stack))
		}
	}

	companion object {
		private val itemMap = mapOf(Pair(Items.WHEAT, 1), Pair(Items.NAUTILUS_SHELL, 2), Pair(Items.BELL, 3), Pair(Items.TRIPWIRE_HOOK, 4), Pair(Items.GLASS, 5))
	}
}