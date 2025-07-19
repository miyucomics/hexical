package miyucomics.hexical.features.specklikes.mesh

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.inits.HexicalAdvancements
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d

object OpConjureMesh : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getVec3(0, argc)
		env.assertVecInRange(position)
		return SpellAction.Result(Spell(position), MediaConstants.DUST_UNIT, listOf())
	}

	private data class Spell(val position: Vec3d) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {}
		override fun cast(env: CastingEnvironment, image: CastingImage): CastingImage {
			if (env.castingEntity is ServerPlayerEntity)
				HexicalAdvancements.AR.trigger(env.castingEntity as ServerPlayerEntity)

			val mesh = MeshEntity(env.world)
			mesh.setPosition(position)
			mesh.setPigment(env.pigment)
			mesh.setSize(1f)
			mesh.setThickness(1f)
			env.world.spawnEntity(mesh)

			return image.copy(stack = image.stack.toList().plus(EntityIota(mesh)))
		}
	}
}