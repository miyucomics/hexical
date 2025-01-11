package miyucomics.hexical.casting.patterns.conjure

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getPositiveDoubleUnderInclusive
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import miyucomics.hexical.entities.SpikeEntity
import miyucomics.hexical.inits.HexicalEntities
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import kotlin.math.floor

class OpConjureSpike : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val position = args.getBlockPos(0, argc)
		env.assertPosInRange(position)
		if (env.world.getBlockState(position).isAir)
			throw MishapInvalidIota.of(args[0], 0, "solid_block")
		val offset = args.getBlockPos(1, argc)
		val direction = Direction.fromVector(offset.x, offset.y, offset.z) ?: throw MishapInvalidIota.of(args[1], 1, "axis_vector")
		if (env.world.getEntitiesByType(HexicalEntities.SPIKE_ENTITY, Box.of(Vec3d.ofCenter(position.add(offset)), 0.9, 0.9, 0.9)) { true }.size > 0)
			return SpellAction.Result(Noop(position), 0, listOf())
		val delay = floor(args.getPositiveDoubleUnderInclusive(2, 10.0, argc) * 20.0).toInt()
		val spawn = Vec3d.ofBottomCenter(position).add(Vec3d.of(direction.vector))
		return SpellAction.Result(Spell(spawn, direction, delay), MediaConstants.SHARD_UNIT, listOf(ParticleSpray.cloud(spawn, 1.0)))
	}

	private data class Noop(val position: BlockPos) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {}
	}

	private data class Spell(val position: Vec3d, val direction: Direction, val delay: Int) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val spike = SpikeEntity(env.world, position.x, position.y, position.z, direction, delay)
			val caster = env.castingEntity
			if (caster is PlayerEntity)
				spike.setConjurer(caster)
			env.world.spawnEntity(spike)
		}
	}
}