package miyucomics.hexical.entities

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.registry.HexicalDamageTypes
import miyucomics.hexical.registry.HexicalEntities
import net.minecraft.entity.*
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.particle.ItemStackParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.*
import kotlin.math.pow

@OptIn(ExperimentalStdlibApi::class)
class SpikeEntity(entityType: EntityType<SpikeEntity>, world: World) : Entity(entityType, world) {
	private var timer = 0
	private var conjurerUUID: UUID? = null
	private var conjurer: PlayerEntity? = null

	constructor(world: World, x: Double, y: Double, z: Double, direction: Direction, delay: Int) : this(HexicalEntities.SPIKE_ENTITY, world) {
		this.setDirection(direction)
		this.setPosition(x, y, z)
		this.timer = -delay
		this.dataTracker.set(timerDataTracker, this.timer)
	}

	override fun tick() {
		this.timer += 1
		if (this.timer == EMERGE_LENGTH) {
			this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 1f)
			for (livingEntity in world.getNonSpectatingEntities(LivingEntity::class.java, boundingBox))
				this.damage(livingEntity)
		}
		if (this.timer > EMERGE_LENGTH + STAY_LENGTH + DISAPPEAR_LENGTH && !world.isClient) {
			(world as ServerWorld).spawnParticles(ItemStackParticleEffect(ParticleTypes.ITEM, ItemStack(Items.AMETHYST_BLOCK, 1)), this.x, this.y, this.z, 8, HexicalMain.RANDOM.nextGaussian() / 20f, HexicalMain.RANDOM.nextGaussian() / 20f, HexicalMain.RANDOM.nextGaussian() / 20f, HexicalMain.RANDOM.nextGaussian() / 10f)
			world.playSound(null, this.blockPos, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.NEUTRAL, 0.25f, 1.5f)
			this.discard()
		}
	}

	private fun damage(target: LivingEntity) {
		val direction = Direction.byId(this.dataTracker.get(directionDataTracker)).unitVector
		direction.scale(0.5f)
		target.addVelocity(direction.x.toDouble(), direction.y.toDouble() + 0.5f, direction.z.toDouble())
		if (!target.isAlive || target.isInvulnerable)
			return
		target.damage(HexicalDamageTypes.spike(this, getConjurer()), 6f)
	}

	private fun setDirection(direction: Direction) {
		this.dataTracker.set(directionDataTracker, direction.id)
		if (direction.axis.isHorizontal) {
			this.pitch = 0.0f
			this.yaw = (direction.horizontal * 90).toFloat()
		} else {
			this.pitch = (-90 * direction.direction.offset()).toFloat()
			this.yaw = 0.0f
		}
		this.prevPitch = this.pitch
		this.prevYaw = this.yaw
	}

	fun getAnimationProgress(): Float {
		if (this.timer < 0)
			return 0f
		if (this.timer in 0..<EMERGE_LENGTH)
			return (this.timer / EMERGE_LENGTH.toFloat()).pow(5f)
		if (this.timer in EMERGE_LENGTH..<EMERGE_LENGTH + STAY_LENGTH)
			return 1f
		if (this.timer >= EMERGE_LENGTH + STAY_LENGTH)
			return 1f - (this.timer - EMERGE_LENGTH - STAY_LENGTH) / DISAPPEAR_LENGTH.toFloat()
		return 1f
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		this.timer = nbt.getInt("timer")
		if (nbt.containsUuid("conjurer"))
			this.conjurerUUID = nbt.getUuid("conjurer")
		else {
			this.conjurer = null
			this.conjurerUUID = null
		}
		this.dataTracker.set(timerDataTracker, this.timer)
		this.setDirection(Direction.byId(nbt.getInt("direction")))
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		nbt.putInt("timer", this.timer)
		if (this.conjurerUUID != null)
			nbt.putUuid("conjurer", this.conjurerUUID)
		nbt.putInt("direction", this.dataTracker.get(directionDataTracker))
	}

	fun setConjurer(player: PlayerEntity) {
		this.conjurer = player
		this.conjurerUUID = player.uuid
	}

	private fun getConjurer(): PlayerEntity? {
		if (this.conjurer != null)
			return this.conjurer
		if (this.conjurerUUID == null)
			return null
		val possible = this.world.getPlayerByUuid(conjurerUUID)
		if (possible != null) {
			this.conjurer = possible
			return possible
		}
		return null
	}

	fun getDirection(): Direction = Direction.byId(this.dataTracker.get(directionDataTracker))
	override fun getEyeHeight(pose: EntityPose, dimensions: EntityDimensions) = 0.5f
	override fun createSpawnPacket() = EntitySpawnS2CPacket(this)

	override fun initDataTracker() {
		this.dataTracker.startTracking(directionDataTracker, 0)
		this.dataTracker.startTracking(timerDataTracker, 0)
	}

	override fun onTrackedDataSet(data: TrackedData<*>?) {
		if (data == timerDataTracker)
			this.timer = this.dataTracker.get(timerDataTracker)
	}

	companion object {
		private const val EMERGE_LENGTH = 10
		private const val STAY_LENGTH = 10
		private const val DISAPPEAR_LENGTH = 20
		private val directionDataTracker: TrackedData<Int> = DataTracker.registerData(SpikeEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
		private val timerDataTracker: TrackedData<Int> = DataTracker.registerData(SpikeEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
	}
}