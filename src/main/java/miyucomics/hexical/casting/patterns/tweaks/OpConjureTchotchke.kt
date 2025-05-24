package miyucomics.hexical.casting.patterns.tweaks

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.registry.HexicalItems
import miyucomics.hexical.utils.CastingUtils
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Vec3d

class OpConjureTchotchke : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getVec3(0, argc)
		env.assertVecInRange(position)
		val battery = args.getPositiveDouble(1, argc)
		return SpellAction.Result(Spell(position, (battery * MediaConstants.DUST_UNIT).toLong()), MediaConstants.CRYSTAL_UNIT + MediaConstants.DUST_UNIT * battery.toInt(), listOf(ParticleSpray.burst(position, 1.0)))
	}

	private data class Spell(val position: Vec3d, val battery: Long) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			env.world.spawnEntity(ItemEntity(env.world, position.x, position.y, position.z, ItemStack(HexicalItems.TCHOTCHKE_ITEM)))
		}
	}
}