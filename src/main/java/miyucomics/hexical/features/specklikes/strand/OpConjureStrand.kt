package miyucomics.hexical.features.specklikes.strand

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.inits.HexicalAdvancements
import net.minecraft.command.argument.EntityAnchorArgumentType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d

object OpConjureStrand : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getVec3(1, argc)
		env.assertVecInRange(position)
		return SpellAction.Result(Spell(position, args.getVec3(2, argc), args[0]), MediaConstants.DUST_UNIT / 100, listOf())
	}

	private data class Spell(val position: Vec3d, val rotation: Vec3d, val iota: Iota) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {}
		override fun cast(env: CastingEnvironment, image: CastingImage): CastingImage {
			if (env.castingEntity is ServerPlayerEntity)
				HexicalAdvancements.AR.trigger(env.castingEntity as ServerPlayerEntity)

			val speck = StrandEntity(env.world).apply {
				setPosition(position.subtract(0.0, standingEyeHeight.toDouble(), 0.0))
				lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, pos.add(rotation))
			}

			env.world.spawnEntity(speck)

			return image.copy(stack = image.stack.toList().plus(EntityIota(speck)))
		}
	}
}