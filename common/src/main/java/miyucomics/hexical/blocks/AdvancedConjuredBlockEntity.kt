package miyucomics.hexical.blocks

import at.petrak.hexcasting.api.block.HexBlockEntity
import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Vec3Iota
import at.petrak.hexcasting.api.utils.vecFromNBT
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
	private val colorizerTag: String = "colorizer"
	private val bouncyTag: String = "bouncy"
	private val slipperyTag: String = "slippery"
	private val volatileTag: String = "volatile"
	private var colorizer: FrozenColorizer = FrozenColorizer.DEFAULT.get()
	var bouncy: Boolean = false
	var slippery: Boolean = false
	var volatile: Boolean = false

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
		tag.put(colorizerTag, colorizer.serializeToNBT())
		tag.putBoolean(bouncyTag, this.bouncy)
		tag.putBoolean(slipperyTag, this.slippery)
		tag.putBoolean(volatileTag, this.volatile)
	}

	override fun loadModData(tag: NbtCompound) {
		this.colorizer = FrozenColorizer.fromNBT(tag.getCompound(colorizerTag))
		this.bouncy = tag.getBoolean(bouncyTag)
		this.slippery = tag.getBoolean(slipperyTag)
		this.volatile = tag.getBoolean(volatileTag)
	}

	fun setProperty(property: String) {
		when (property) {
			"bouncy" -> this.bouncy = true
			"slippery" -> this.slippery = true
			"volatile" -> this.volatile = true
		}
		this.sync()
	}

	fun setColorizer(colorizer: FrozenColorizer) {
		this.colorizer = colorizer
		this.sync()
	}
}