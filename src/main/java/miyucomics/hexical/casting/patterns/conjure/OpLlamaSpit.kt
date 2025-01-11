package miyucomics.hexical.casting.patterns.conjure

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.entity.EntityType
import net.minecraft.entity.projectile.FireballEntity
import net.minecraft.entity.projectile.LlamaSpitEntity
import net.minecraft.util.math.Vec3d

class OpLlamaSpit : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val pos = args.getVec3(0, argc)
		env.assertVecInRange(pos)
		return SpellAction.Result(Spell(pos), MediaConstants.DUST_UNIT / 4, listOf())
	}

	private data class Spell(val position: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {}
		override fun cast(env: CastingEnvironment, image: CastingImage): CastingImage {
			val spit = LlamaSpitEntity(EntityType.LLAMA_SPIT, env.world)
			spit.setPosition(position)
			spit.owner = env.castingEntity
			env.world.spawnEntity(spit)
			return image.copy(stack = image.stack.toList().plus(EntityIota(spit)))
		}
	}
}