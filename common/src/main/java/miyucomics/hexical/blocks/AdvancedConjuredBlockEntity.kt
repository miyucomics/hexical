package miyucomics.hexical.blocks

import at.petrak.hexcasting.api.block.HexBlockEntity
import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.spell.getPositiveInt
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.common.particles.ConjureParticleOptions
import miyucomics.hexical.registry.HexicalBlocks
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.util.*

class AdvancedConjuredBlockEntity(pos: BlockPos?, state: BlockState?) : HexBlockEntity(HexicalBlocks.ADVANCED_CONJURED_BLOCK_ENTITY, pos, state) {
	private val random = Random()
	private var colorizer: FrozenColorizer = FrozenColorizer.DEFAULT.get()
	var bouncy: Boolean = false
	var ephemeral: Boolean = false
	var invisible: Boolean = false
	var volatile: Boolean = false
	var lifespan: Int = 0

	fun walkParticle(entity: Entity) {
		for (i in 0..2) {
			val color = colorizer.getColor(entity.age.toFloat(), entity.pos.add(
				Vec3d(random.nextDouble(), random.nextDouble(), random.nextDouble()).multiply(
				random.nextDouble() * 3)))
			assert(world != null)
			world!!.addParticle(
				ConjureParticleOptions(color, false),
				entity.x + (random.nextFloat() * 0.6) - 0.3,
				getPos().y + (random.nextFloat() * 0.05) + 0.95,
				entity.z + (random.nextFloat() * 0.6) - 0.3,
				random.nextFloat(-0.02f, 0.02f).toDouble(),
				random.nextFloat(0.02f).toDouble(),
				random.nextFloat(-0.02f, 0.02f).toDouble()
			)
		}
	}

	fun particleEffect() {
		val color = colorizer.getColor(
			random.nextFloat() * 16384, Vec3d(random.nextDouble(), random.nextDouble(), random.nextDouble()).multiply(
				random.nextDouble() * 3))
		assert(world != null)
		if (random.nextFloat() < 0.2) {
			world!!.addParticle(
				ConjureParticleOptions(color, false),
				getPos().x.toDouble() + random.nextFloat(),
				getPos().y.toDouble() + random.nextFloat(),
				getPos().z.toDouble() + random.nextFloat(),
				random.nextFloat(-0.02f, 0.02f).toDouble(),
				random.nextFloat(-0.02f, 0.02f).toDouble(),
				random.nextFloat(-0.02f, 0.02f).toDouble()
			)
		}
	}

	override fun saveModData(tag: NbtCompound) {
		tag.put("colorizer", colorizer.serializeToNBT())
		tag.putBoolean("bouncy", this.bouncy)
		tag.putBoolean("ephemeral", this.ephemeral)
		tag.putBoolean("invisible", this.invisible)
		tag.putBoolean("volatile", this.volatile)
		tag.putInt("lifespan", this.lifespan)
	}

	override fun loadModData(tag: NbtCompound) {
		this.colorizer = FrozenColorizer.fromNBT(tag.getCompound("colorizer"))
		this.bouncy = tag.getBoolean("bouncy")
		this.ephemeral = tag.getBoolean("ephemeral")
		this.invisible = tag.getBoolean("invisible")
		this.volatile = tag.getBoolean("volatile")
		this.lifespan = tag.getInt("lifespan")
	}

	fun setProperty(property: String, args: List<Iota>) {
		when (property) {
			"bouncy" -> this.bouncy = true
			"ephemeral" -> {
				this.ephemeral = true
				this.lifespan = args.getPositiveInt(0, args.size)
			}
			"invisible" -> this.invisible = true
			"volatile" -> this.volatile = true
		}
		this.sync()
	}

	fun setColorizer(colorizer: FrozenColorizer) {
		this.colorizer = colorizer
		this.sync()
	}
}