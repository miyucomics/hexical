package miyucomics.hexical.entities

import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.api.spell.iota.PatternIota
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.registry.HexicalEntities
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.block.AbstractRedstoneGateBlock
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
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
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.GameRules
import net.minecraft.world.World
import java.lang.IllegalStateException

class LivingScrollEntity(entityType: EntityType<LivingScrollEntity?>?, world: World?) : AbstractDecorationEntity(entityType, world) {
	var patterns: MutableList<NbtCompound> = mutableListOf()
	var cachedPattern = HexPattern.fromAngles("", HexDir.EAST) // client-only
	companion object {
		private val sizeDataTracker: TrackedData<Int> = DataTracker.registerData(LivingScrollEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
		private val renderDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(LivingScrollEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
	}

	override fun initDataTracker() {
		this.dataTracker.startTracking(sizeDataTracker, 1)
		this.dataTracker.startTracking(renderDataTracker, NbtCompound())
	}

	override fun onTrackedDataSet(data: TrackedData<*>) {
		when (data) {
			sizeDataTracker -> this.updateAttachmentPosition()
			renderDataTracker -> this.cachedPattern = HexPattern.fromNBT(dataTracker.get(renderDataTracker))
			else -> {}
		}
	}

	override fun canStayAttached(): Boolean {
		if (!world.isSpaceEmpty(this))
			return false
		val blockPos = attachmentPos.offset(facing.opposite)
		val direction = facing.rotateYCounterclockwise()
		val mutable = BlockPos.Mutable()
		val size = dataTracker.get(sizeDataTracker)
		for (i in 0 until size) {
			for (j in 0 until size) {
				val m = (size - 1) / -2
				val n = (size - 1) / -2
				mutable.set(blockPos).move(direction, i + m).move(Direction.UP, j + n)
				val blockState = world.getBlockState(mutable)
				if (blockState.material.isSolid || AbstractRedstoneGateBlock.isRedstoneGate(blockState)) continue
				return false
			}
		}
		return world.getOtherEntities(this, this.boundingBox, PREDICATE).isEmpty()
	}

	public override fun setFacing(facing: Direction) {
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

	constructor(world: World, position: BlockPos, dir: Direction, size: Int, patterns: MutableList<NbtCompound>) : this(HexicalEntities.LIVING_SCROLL_ENTITY, world) {
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
		this.dataTracker.set(renderDataTracker, patterns[((world.time / 20).toInt() % patterns.size)])
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound?) {
		nbt!!.putByte("direction", facing.id.toByte())
		nbt.putInt("size", this.dataTracker.get(sizeDataTracker))

		val data = NbtList()
		for (pattern in this.patterns)
			data.add(pattern)
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
			this.patterns.add(pattern as NbtCompound)
		updateRender()

		super.readCustomDataFromNbt(nbt)
	}

	override fun onPlace() = playSound(SoundEvents.ENTITY_PAINTING_PLACE, 1.0F, 1.0F)
	override fun onBreak(entity: Entity?) {
		this.playSound(SoundEvents.ENTITY_PAINTING_BREAK, 1.0f, 1.0f)
		if (this.world.gameRules.getBoolean(GameRules.DO_ENTITY_DROPS)) {
			if (entity is PlayerEntity && entity.abilities.creativeMode)
				return
			val stack = ItemStack(when (this.dataTracker.get(sizeDataTracker)) {
				1 -> HexicalItems.SMALL_LIVING_SCROLL_ITEM
				2 -> HexicalItems.MEDIUM_LIVING_SCROLL_ITEM
				3 -> HexicalItems.LARGE_LIVING_SCROLL_ITEM
				else -> throw IllegalStateException()
			})
			val constructed = mutableListOf<PatternIota>()
			for (pattern in this.patterns)
				constructed.add(PatternIota(HexPattern.fromNBT(pattern)))
			stack.orCreateNbt.putCompound("patterns", HexIotaTypes.serialize(ListIota(constructed.toList())))
			this.dropStack(stack)
		}
	}

	override fun getPickBlockStack() = ItemStack(when (this.dataTracker.get(sizeDataTracker)) {
			1 -> HexicalItems.SMALL_LIVING_SCROLL_ITEM
			2 -> HexicalItems.MEDIUM_LIVING_SCROLL_ITEM
			3 -> HexicalItems.LARGE_LIVING_SCROLL_ITEM
			else -> throw IllegalStateException("Invalid size")
		})

	fun getSize(): Int = this.dataTracker.get(sizeDataTracker)
	override fun getSyncedPos(): Vec3d = Vec3d.of(this.attachmentPos)
	override fun getWidthPixels() = 16 * this.dataTracker.get(sizeDataTracker)
	override fun getHeightPixels() = 16 * this.dataTracker.get(sizeDataTracker)
	override fun getEyeHeight(pose: EntityPose?, dimensions: EntityDimensions?) = 0f
	override fun refreshPositionAndAngles(x: Double, y: Double, z: Double, yaw: Float, pitch: Float) = this.setPosition(x, y, z)
	override fun updateTrackedPositionAndAngles(x: Double, y: Double, z: Double, yaw: Float, pitch: Float, interpolationSteps: Int, interpolate: Boolean) = this.setPosition(x, y, z)

	override fun createSpawnPacket() =  EntitySpawnS2CPacket(this, facing.id, this.decorationBlockPos)
	override fun onSpawnPacket(packet: EntitySpawnS2CPacket) {
		super.onSpawnPacket(packet)
		this.setFacing(Direction.byId(packet.entityData))
	}
}