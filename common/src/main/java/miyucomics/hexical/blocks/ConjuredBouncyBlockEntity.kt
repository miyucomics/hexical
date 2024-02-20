package miyucomics.hexical.blocks

import at.petrak.hexcasting.api.block.HexBlockEntity
import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.common.blocks.BlockConjured
import at.petrak.hexcasting.common.particles.ConjureParticleOptions
import miyucomics.hexical.registry.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.util.*

class ConjuredBouncyBlockEntity(pos: BlockPos?, state: BlockState?) : HexBlockEntity(HexicalBlocks.CONJURED_BOUNCY_BLOCK_ENTITY, pos, state) {
	private var colorizer: FrozenColorizer = FrozenColorizer.DEFAULT.get()

	fun walkParticle(entity: Entity) {
		for (i in 0..2) {
			val color = colorizer.getColor(entity.age.toFloat(), entity.pos.add(Vec3d(RANDOM.nextDouble(), RANDOM.nextDouble(), RANDOM.nextDouble()).multiply(RANDOM.nextDouble() * 3)))
			assert(world != null)
			world!!.addParticle(
				ConjureParticleOptions(color, false),
				entity.x + (RANDOM.nextFloat() * 0.6) - 0.3,
				getPos().y + (RANDOM.nextFloat() * 0.05) + 0.95,
				entity.z + (RANDOM.nextFloat() * 0.6) - 0.3,
				RANDOM.nextFloat(-0.02f, 0.02f).toDouble(),
				RANDOM.nextFloat(0.02f).toDouble(),
				RANDOM.nextFloat(-0.02f, 0.02f).toDouble()
			)
		}
	}

	fun particleEffect() {
		val color = colorizer.getColor(RANDOM.nextFloat() * 16384, Vec3d(RANDOM.nextDouble(), RANDOM.nextDouble(), RANDOM.nextDouble()).multiply(RANDOM.nextDouble() * 3))
		assert(world != null)
		if (RANDOM.nextFloat() < 0.2) {
			world!!.addParticle(
				ConjureParticleOptions(color, false),
				getPos().x.toDouble() + RANDOM.nextFloat(),
				getPos().y.toDouble() + RANDOM.nextFloat(),
				getPos().z.toDouble() + RANDOM.nextFloat(),
				RANDOM.nextFloat(-0.02f, 0.02f).toDouble(),
				RANDOM.nextFloat(-0.02f, 0.02f).toDouble(),
				RANDOM.nextFloat(-0.02f, 0.02f).toDouble()
			)
		}
	}

	fun landParticle(entity: Entity, number: Int) {
		for (i in 0 until number * 2) {
			val color = colorizer.getColor(entity.age.toFloat(), entity.pos.add(Vec3d(RANDOM.nextDouble(), RANDOM.nextDouble(), RANDOM.nextDouble()).multiply(RANDOM.nextDouble() * 3)))
			assert(world != null)
			world!!.addParticle(
				ConjureParticleOptions(color, false),
				entity.x + (RANDOM.nextFloat() * 0.8) - 0.2,
				getPos().y + (RANDOM.nextFloat() * 0.05) + 0.95,
				entity.z + (RANDOM.nextFloat() * 0.8) - 0.2,
				0.0, 0.0, 0.0
			)
		}
	}

	override fun saveModData(tag: NbtCompound) {
		tag.put(TAG_COLORIZER, colorizer.serializeToNBT())
	}

	override fun loadModData(tag: NbtCompound) {
		this.colorizer = FrozenColorizer.fromNBT(tag.getCompound(TAG_COLORIZER))
	}

	fun getColorizer(): FrozenColorizer {
		return this.colorizer
	}

	fun setColorizer(colorizer: FrozenColorizer) {
		this.colorizer = colorizer
		this.sync()
	}

	companion object {
		private val RANDOM = Random()
		const val TAG_COLORIZER: String = "tag_colorizer"
	}
}