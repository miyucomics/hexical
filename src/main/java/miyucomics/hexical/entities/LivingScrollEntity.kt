package miyucomics.hexical.entities

import at.petrak.hexcasting.api.spell.math.HexPattern
import miyucomics.hexical.registry.HexicalEntities
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityPose
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.decoration.AbstractDecorationEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.network.Packet
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.GameRules
import net.minecraft.world.World

class LivingScrollEntity(entityType: EntityType<LivingScrollEntity?>?, world: World?) : AbstractDecorationEntity(entityType, world) {
	companion object {
		private val sizeDataTracker: TrackedData<Int> = DataTracker.registerData(LivingScrollEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
		private val renderDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(LivingScrollEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
	}

	var patterns: MutableList<HexPattern> = mutableListOf()

	override fun getTargetingMargin() = 0f
	override fun getEyeHeight(pose: EntityPose?) = 0f

	override fun createSpawnPacket(): Packet<*> {
		return EntitySpawnS2CPacket(this, facing.id, this.decorationBlockPos)
	}

	override fun initDataTracker() {
		this.dataTracker.startTracking(sizeDataTracker, 1)
		this.dataTracker.startTracking(renderDataTracker, NbtCompound())
	}

	override fun canStayAttached(): Boolean {
		if (!world.isSpaceEmpty(this))
			return false
		val blockState = world.getBlockState(attachmentPos.offset(facing.opposite))
		return blockState.material.isSolid
	}

	override fun setFacing(facing: Direction) {
		this.facing = facing
		if (facing.axis.isHorizontal) {
			this.pitch = 0.0f
			this.yaw = (this.facing.horizontal * 90).toFloat()
		} else {
			this.pitch = (-90 * facing.direction.offset()).toFloat()
			this.yaw = 0.0f
		}
		this.prevPitch = this.pitch
		this.prevYaw = this.yaw
		updateAttachmentPosition()
	}

	override fun updateAttachmentPosition() {
		if (this.facing == null) {
			return
		}
		val e = attachmentPos.x.toDouble() + 0.5 - facing.offsetX.toDouble() * 0.46875
		val f = attachmentPos.y.toDouble() + 0.5 - facing.offsetY.toDouble() * 0.46875
		val g = attachmentPos.z.toDouble() + 0.5 - facing.offsetZ.toDouble() * 0.46875
		this.setPos(e, f, g)
		var h = this.widthPixels.toDouble()
		var i = this.heightPixels.toDouble()
		var j = this.widthPixels.toDouble()
		when (facing.axis) {
			Direction.Axis.X -> {
				h = 1.0
			}
			Direction.Axis.Y -> {
				i = 1.0
			}
			Direction.Axis.Z -> {
				j = 1.0
			}
			null -> throw IllegalStateException()
		}
		this.boundingBox = Box(e - h / 32, f - i / 32, g - j / 32, e + h / 32, f + i / 32, g + j / 32)
	}

	override fun refreshPositionAndAngles(x: Double, y: Double, z: Double, yaw: Float, pitch: Float) {
		this.setPosition(x, y, z)
	}

	override fun updateTrackedPositionAndAngles(x: Double, y: Double, z: Double, yaw: Float, pitch: Float, interpolationSteps: Int, interpolate: Boolean) {
		this.setPosition(x, y, z)
	}

	override fun onSpawnPacket(packet: EntitySpawnS2CPacket) {
		super.onSpawnPacket(packet)
		this.setFacing(Direction.byId(packet.entityData))
	}

	override fun getSyncedPos(): Vec3d {
		return Vec3d.of(this.attachmentPos)
	}

	constructor(world: World, position: BlockPos, dir: Direction, size: Int, patterns: MutableList<HexPattern>) : this(HexicalEntities.LIVING_SCROLL_ENTITY, world) {
		this.attachmentPos = position
		this.patterns = patterns
		this.dataTracker.set(sizeDataTracker, size)
		setFacing(dir)
		updateRender()
	}

	override fun tick() {
		if ((world.time % 20).toInt() == 0 && patterns.isNotEmpty())
			updateRender()
		super.tick()
	}

	private fun updateRender() {
		this.dataTracker.set(renderDataTracker, patterns[((world.time / 20).toInt() % patterns.size)].serializeToNBT())
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound?) {
		nbt!!.putByte("direction", facing.id.toByte())
		nbt.putInt("size", this.dataTracker.get(sizeDataTracker))

		val data = NbtList()
		for (pattern in patterns)
			data.add(pattern.serializeToNBT())
		nbt.put("patterns", data)

		super.writeCustomDataToNbt(nbt)
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound?) {
		this.facing = Direction.byId(nbt!!.getByte("direction").toInt())
		this.dataTracker.set(sizeDataTracker, nbt.getInt("size"))
		setFacing(this.facing)
		updateAttachmentPosition()

		val data = nbt.get("patterns") as NbtList
		this.patterns = mutableListOf()
		for (pattern in data)
			this.patterns.add(HexPattern.fromNBT(pattern as NbtCompound))
		updateRender()

		super.readCustomDataFromNbt(nbt)
	}

	override fun onPlace() = playSound(SoundEvents.ENTITY_PAINTING_PLACE, 1.0F, 1.0F)
	override fun onBreak(entity: Entity?) {
		this.playSound(SoundEvents.ENTITY_PAINTING_BREAK, 1.0f, 1.0f)
		if (this.world.gameRules.getBoolean(GameRules.DO_ENTITY_DROPS)) {
			if (entity is PlayerEntity && entity.abilities.creativeMode)
				return
		}
	}

	fun getSize(): Int {
		return this.dataTracker.get(sizeDataTracker)
	}

	fun getRender(): HexPattern {
		return HexPattern.fromNBT(dataTracker.get(renderDataTracker))
	}

	override fun getWidthPixels() = 16 * this.dataTracker.get(sizeDataTracker)
	override fun getHeightPixels() = 16 * this.dataTracker.get(sizeDataTracker)
}