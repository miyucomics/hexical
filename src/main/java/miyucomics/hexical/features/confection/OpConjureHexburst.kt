package miyucomics.hexical.features.confection

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.putCompound
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.misc.CastingUtils
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Vec3d

class OpConjureHexburst : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getVec3(0, argc)
		env.assertVecInRange(position)
		val iota = args[1]
		CastingUtils.assertNoTruename(iota, env)
		return SpellAction.Result(Spell(position, iota), MediaConstants.DUST_UNIT, listOf(ParticleSpray.burst(position, 1.0)))
	}

	private data class Spell(val position: Vec3d, val iota: Iota) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val stack = ItemStack(HexicalItems.HEXBURST_ITEM, 1)
			stack.orCreateNbt.putCompound("iota", IotaType.serialize(iota))
			env.world.spawnEntity(ItemEntity(env.world, position.x, position.y, position.z, stack))
		}
	}
}