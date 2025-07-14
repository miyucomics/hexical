package miyucomics.hexical.casting.actions.conjure

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.inits.HexicalItems
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class OpConjureCompass : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getVec3(0, argc)
		env.assertVecInRange(position)
		val target = args.getBlockPos(1, argc)
		return SpellAction.Result(Spell(position, target), MediaConstants.DUST_UNIT * 3, listOf(ParticleSpray.burst(position, 1.0)))
	}

	private data class Spell(val position: Vec3d, val target: BlockPos) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val stack = ItemStack(HexicalItems.CONJURED_COMPASS_ITEM, 1)
			val nbt = stack.orCreateNbt
			nbt.putInt("x", target.x)
			nbt.putInt("y", target.y)
			nbt.putInt("z", target.z)
			nbt.putString("dimension", env.world.dimensionKey.value.toString())
			env.world.spawnEntity(ItemEntity(env.world, position.x, position.y, position.z, stack))
		}
	}
}