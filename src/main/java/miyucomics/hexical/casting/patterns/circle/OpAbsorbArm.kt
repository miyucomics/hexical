package miyucomics.hexical.casting.patterns.circle

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.circle.MishapNoSpellCircle
import miyucomics.hexical.blocks.PedestalBlockEntity
import miyucomics.hexical.casting.mishaps.OutsideCircleMishap
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class OpAbsorbArm : SpellAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env !is CircleCastEnv)
			throw MishapNoSpellCircle()

		val circle = env.impetus ?: throw MishapNoSpellCircle()
		val bounds = circle.executionState!!.bounds

		val pedestal = args.getBlockPos(0, argc)
		if (!bounds.contains(Vec3d.ofCenter(pedestal)))
			throw OutsideCircleMishap()
		if (env.world.getBlockEntity(pedestal) !is PedestalBlockEntity)
			throw MishapBadBlock.of(pedestal, "pedestal")

		return SpellAction.Result(Spell(pedestal), 0, listOf(ParticleSpray.burst(Vec3d.ofCenter(pedestal), 1.0)))
	}

	private data class Spell(val pedestal: BlockPos) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {}
		override fun cast(env: CastingEnvironment, image: CastingImage): CastingImage? {
			val newImage = (env as CircleCastEnv).circleState().currentImage.copy()
			newImage.userData.putIntArray("impetusHand", listOf(pedestal.x, pedestal.y, pedestal.z))
			return newImage
		}
	}
}