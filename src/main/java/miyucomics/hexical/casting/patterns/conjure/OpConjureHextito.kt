package miyucomics.hexical.casting.patterns.conjure

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Vec3d

class OpConjureHextito : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getVec3(0, argc)
		env.assertVecInRange(position)
		args.getList(1, argc)
		CastingUtils.assertNoTruename(args[1], env.caster)
		return SpellAction.Result(Spell(position, args[1]), MediaConstants.DUST_UNIT * 2, listOf(ParticleSpray.burst(position, 1.0)))
	}

	private data class Spell(val position: Vec3d, val hex: Iota) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val stack = ItemStack(HexicalItems.HEXTITO_ITEM, 1)
			stack.orCreateNbt.putCompound("hex", IotaType.serialize(hex))
			val entity = ItemEntity(env.world, position.x, position.y, position.z, stack)
			entity.setPickupDelay(1) // should be nearly imperceptible but allows for hextito quines
			env.world.spawnEntity(entity)
		}
	}
}