package miyucomics.hexical.casting.patterns

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.entities.SpikeEntity
import miyucomics.hexical.interfaces.CastingContextMinterface
import miyucomics.hexical.registry.HexicalEntities
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

class OpSpike : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val position = args.getBlockPos(0, argc)
		ctx.assertVecInRange(position)
		val offset = args.getBlockPos(1, argc)
		val direction = Direction.fromVector(offset) ?: throw MishapInvalidIota.of(args[1], 1, "axis_vector")
		if (ctx.world.getEntitiesByType(HexicalEntities.SPIKE_ENTITY, Box.of(Vec3d.ofCenter(position.add(offset)), 0.9, 0.9, 0.9)) { true }.size > 0)
			return Triple(Noop(position), 0, listOf())
		return Triple(Spell(position, direction), MediaConstants.SHARD_UNIT, listOf())
	}

	private data class Noop(val position: BlockPos) : RenderedSpell {
		override fun cast(ctx: CastingContext) {}
	}

	private data class Spell(val position: BlockPos, val direction: Direction) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val position = Vec3d.ofBottomCenter(position).add(Vec3d.of(direction.vector))
			ctx.world.spawnEntity(SpikeEntity(ctx.world, position.x, position.y, position.z, direction))
		}
	}
}