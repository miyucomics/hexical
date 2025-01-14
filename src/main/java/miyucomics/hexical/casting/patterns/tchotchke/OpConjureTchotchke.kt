package miyucomics.hexical.casting.patterns.tchotchke

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
import net.minecraft.util.math.Vec3d

class OpConjureTchotchke : SpellAction {
	override val argc = 4
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getVec3(0, argc)
		env.assertVecInRange(position)
		val battery = args.getPositiveDouble(1, argc)
		val rank = args.getInt(2, argc)
		if (rank <= 0)
			throw MishapInvalidIota.of(args[2], 1, "integer_natural")
		val instructions = args.getList(3, argc).toList()
		CastingUtils.assertNoTruename(args[3], env)
		return SpellAction.Result(Spell(position, (battery * MediaConstants.DUST_UNIT).toLong(), rank, instructions), MediaConstants.CRYSTAL_UNIT + MediaConstants.DUST_UNIT * battery.toInt(), listOf(ParticleSpray.burst(position, 1.0)))
	}

	private data class Spell(val position: Vec3d, val battery: Long, val rank: Int, val instructions: List<Iota>) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val stack = ItemStack(HexicalItems.TCHOTCHKE_ITEM)
			stack.orCreateNbt.putInt("rank", rank)
			val hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(stack)
			hexHolder?.writeHex(instructions, env.pigment, battery)
			env.world.spawnEntity(ItemEntity(env.world, position.x, position.y, position.z, stack))
		}
	}
}