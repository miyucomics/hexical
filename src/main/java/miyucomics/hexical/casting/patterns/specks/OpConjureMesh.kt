package miyucomics.hexical.casting.patterns.specks

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.entities.specklikes.MeshEntity
import miyucomics.hexical.inits.HexicalAdvancements
import net.minecraft.server.network.ServerPlayerEntity

class OpConjureMesh : ConstMediaAction {
	override val argc = 1
	override val mediaCost = MediaConstants.DUST_UNIT
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val position = args.getVec3(0, argc)
		env.assertVecInRange(position)

		val mesh = MeshEntity(env.world)
		mesh.setPosition(position.subtract(0.0, mesh.standingEyeHeight.toDouble(), 0.0))
		mesh.setPigment(env.pigment)
		mesh.setSize(1f)
		mesh.setThickness(1f)
		env.world.spawnEntity(mesh)

		if (env.castingEntity is ServerPlayerEntity)
			HexicalAdvancements.AR.trigger(env.castingEntity as ServerPlayerEntity)

		return listOf(EntityIota(mesh))
	}
}