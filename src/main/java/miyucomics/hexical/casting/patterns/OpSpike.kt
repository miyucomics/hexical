package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.entities.SpikeEntity
import miyucomics.hexical.registry.HexicalEntities
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import kotlin.math.floor

class OpSpike : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val position = args.getBlockPos(0, argc)
		ctx.assertVecInRange(position)
		if (ctx.world.getBlockState(position).isAir)
			throw MishapInvalidIota.of(args[0], 0, "solid_block")
		val offset = args.getBlockPos(1, argc)
		val direction = Direction.fromVector(offset) ?: throw MishapInvalidIota.of(args[1], 1, "axis_vector")
		if (ctx.world.getEntitiesByType(HexicalEntities.SPIKE_ENTITY, Box.of(Vec3d.ofCenter(position.add(offset)), 0.9, 0.9, 0.9)) { true }.size > 0)
			return Triple(Noop(position), 0, listOf())
		val delay = floor(args.getPositiveDoubleUnderInclusive(2, 10.0, argc) * 20.0).toInt()
		return Triple(Spell(position, direction, delay), MediaConstants.SHARD_UNIT, listOf())
	}

	private data class Noop(val position: BlockPos) : RenderedSpell {
		override fun cast(ctx: CastingContext) {}
	}

	private data class Spell(val position: BlockPos, val direction: Direction, val delay: Int) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val position = Vec3d.ofBottomCenter(position).add(Vec3d.of(direction.vector))
			val spike = SpikeEntity(ctx.world, position.x, position.y, position.z, direction, delay)
			spike.setConjurer(ctx.caster)
			ctx.world.spawnEntity(spike)
		}
	}
}