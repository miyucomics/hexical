package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.math.Vec3d

class OpHorrible : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val pos = args.getVec3(0, argc)
		env.assertVecInRange(pos)
		return SpellAction.Result(Spell(pos), 0, listOf(ParticleSpray.burst(pos, 2.0)))
	}

	private data class Spell(val pos: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val cookie = ItemEntity(env.world, pos.x, pos.y, pos.z, ItemStack(Items.COOKIE), 0.0, 0.0, 0.0)
			cookie.setNoGravity(true)
			env.world.spawnEntity(cookie)
		}
	}
}