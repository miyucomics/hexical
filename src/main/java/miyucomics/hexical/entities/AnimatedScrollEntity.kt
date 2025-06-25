package miyucomics.hexical.entities

import at.petrak.hexcasting.api.addldata.ADIotaHolder
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.api.utils.putList
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.registry.HexicalEntities
import miyucomics.hexical.registry.HexicalItems
import miyucomics.hexical.utils.RenderUtils
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
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import net.minecraft.world.GameRules
import net.minecraft.world.World

class AnimatedScrollEntity(entityType: EntityType<AnimatedScrollEntity>, world: World) : AbstractDecorationEntity(entityType, world), ADIotaHolder {
	var patterns: List<NbtCompound> = listOf()
	var cachedVerts: List<Vec2f> = listOf()

	constructor(world: World) : this(HexicalEntities.ANIMATED_SCROLL_ENTITY, world)

	constructor(world: World, position: BlockPos, dir: Direction, size: Int, patterns: List<NbtCompound>) : this(world) {
		this.attachmentPos = position
		this.patterns = patterns
		this.dataTracker.set(sizeDataTracker, size)
		setFacing(dir)
		if (!world.isClient)
			updateRender()
	}

	override fun tick() {
		super.tick()
		if (!world.isClient && (world.time % 20).toInt() == 0)
			updateRender()
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
				if (blockState.isSolid || AbstractRedstoneGateBlock.isRedstoneGate(blockState)) continue
				return false
			}
		}
		return world.getOtherEntities(this, this.boundingBox.shrink(0.95, 0.95, 0.95), PREDICATE).isEmpty()
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

	private fun updateRender() {
		if (this.patterns.isNotEmpty())
			this.dataTracker.set(patternDataTracker, patterns[((world.time / 20).toInt() % patterns.size)])
		else {
			val compound = NbtCompound()
			compound.putBoolean("empty", true)
			this.dataTracker.set(patternDataTracker, compound)
		}
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		nbt.putInt("direction", facing.id)
		nbt.putInt("state", this.dataTracker.get(stateDataTracker))
		nbt.putInt("color", this.dataTracker.get(colorDataTracker))
		nbt.putBoolean("glow", this.dataTracker.get(glowDataTracker))
		nbt.putInt("size", this.dataTracker.get(sizeDataTracker))

		val data = NbtList()
		for (pattern in this.patterns)
			data.add(pattern)
		nbt.put("patterns", data)

		super.writeCustomDataToNbt(nbt)
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		this.facing = Direction.byId(nbt.getInt("direction"))
		this.dataTracker.set(stateDataTracker, nbt.getInt("state"))
		this.dataTracker.set(glowDataTracker, nbt.getBoolean("glow"))
		this.dataTracker.set(colorDataTracker, nbt.getInt("color"))
		this.dataTracker.set(sizeDataTracker, nbt.getInt("size"))
		setFacing(this.facing)
		updateAttachmentPosition()

		this.patterns = nbt.getList("patterns", NbtElement.COMPOUND_TYPE.toInt()).map { it.asCompound }
		updateRender()

		super.readCustomDataFromNbt(nbt)
	}

	override fun onPlace() = playSound(SoundEvents.ENTITY_PAINTING_PLACE, 1.0F, 1.0F)
	override fun onBreak(entity: Entity?) {
		this.playSound(SoundEvents.ENTITY_PAINTING_BREAK, 1.0f, 1.0f)
		if (this.world.gameRules.getBoolean(GameRules.DO_ENTITY_DROPS)) {
			if (entity is PlayerEntity && entity.abilities.creativeMode)
				return
			val stack = ItemStack(
				when (this.dataTracker.get(sizeDataTracker)) {
					1 -> HexicalItems.SMALL_ANIMATED_SCROLL_ITEM
					2 -> HexicalItems.MEDIUM_ANIMATED_SCROLL_ITEM
					3 -> HexicalItems.LARGE_ANIMATED_SCROLL_ITEM
					else -> throw IllegalStateException()
				}
			)

			val constructed = NbtList()
			this.patterns.forEach { constructed.add(it) }
			stack.orCreateNbt.putList("patterns", constructed)

			stack.orCreateNbt.putBoolean("glow", this.dataTracker.get(glowDataTracker))
			stack.orCreateNbt.putInt("color", this.dataTracker.get(colorDataTracker))
			stack.orCreateNbt.putInt("state", this.dataTracker.get(stateDataTracker))
			this.dropStack(stack)
		}
	}

	override fun getPickBlockStack() = ItemStack(
		when (this.dataTracker.get(sizeDataTracker)) {
			1 -> HexicalItems.SMALL_ANIMATED_SCROLL_ITEM
			2 -> HexicalItems.MEDIUM_ANIMATED_SCROLL_ITEM
			3 -> HexicalItems.LARGE_ANIMATED_SCROLL_ITEM
			else -> throw IllegalStateException("Invalid size")
		}
	)

	override fun getSyncedPos(): Vec3d = Vec3d.of(this.attachmentPos)
	override fun getWidthPixels() = 16 * this.dataTracker.get(sizeDataTracker)
	override fun getHeightPixels() = 16 * this.dataTracker.get(sizeDataTracker)
	override fun getEyeHeight(pose: EntityPose, dimensions: EntityDimensions) = 0f
	override fun refreshPositionAndAngles(x: Double, y: Double, z: Double, yaw: Float, pitch: Float) = this.setPosition(x, y, z)
	override fun updateTrackedPositionAndAngles(x: Double, y: Double, z: Double, yaw: Float, pitch: Float, interpolationSteps: Int, interpolate: Boolean) = this.setPosition(x, y, z)

	override fun createSpawnPacket() = EntitySpawnS2CPacket(this, facing.id, this.decorationBlockPos)
	override fun onSpawnPacket(packet: EntitySpawnS2CPacket) {
		super.onSpawnPacket(packet)
		this.setFacing(Direction.byId(packet.entityData))
	}

	fun setState(state: Int) = this.dataTracker.set(stateDataTracker, state)
	fun makeAncient() = this.dataTracker.set(stateDataTracker, 1)
	fun setColor(color: Int) = this.dataTracker.set(colorDataTracker, color)
	fun toggleGlow() = this.dataTracker.set(glowDataTracker, !this.dataTracker.get(glowDataTracker))

	override fun initDataTracker() {
		this.dataTracker.startTracking(colorDataTracker, (0xff_000000).toInt())
		this.dataTracker.startTracking(glowDataTracker, false)
		this.dataTracker.startTracking(stateDataTracker, 0)
		this.dataTracker.startTracking(sizeDataTracker, 1)
		this.dataTracker.startTracking(patternDataTracker, NbtCompound())
	}

	override fun onTrackedDataSet(data: TrackedData<*>) {
		when (data) {
			sizeDataTracker -> this.updateAttachmentPosition()
			patternDataTracker -> {
				val nbt = dataTracker.get(patternDataTracker)
				this.cachedVerts = if (nbt.contains("empty")) listOf() else RenderUtils.getNormalizedStrokes(HexPattern.fromNBT(nbt), true)
			}
			else -> {}
		}
	}

	companion object {
		private val patternDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(AnimatedScrollEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
		val glowDataTracker: TrackedData<Boolean> = DataTracker.registerData(AnimatedScrollEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
		val colorDataTracker: TrackedData<Int> = DataTracker.registerData(AnimatedScrollEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
		val sizeDataTracker: TrackedData<Int> = DataTracker.registerData(AnimatedScrollEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
		val stateDataTracker: TrackedData<Int> = DataTracker.registerData(AnimatedScrollEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
	}

	override fun readIotaTag(): NbtCompound? {
		val constructed = mutableListOf<PatternIota>()
		for (pattern in this.patterns)
			constructed.add(PatternIota(HexPattern.fromNBT(pattern)))
		return IotaType.serialize(ListIota(constructed.toList()))
	}

	override fun writeIota(iota: Iota?, simulate: Boolean): Boolean {
		if (iota == null) {
			this.patterns = mutableListOf()
			this.updateRender()
			return true
		} else if (iota.type == HexIotaTypes.PATTERN) {
			this.patterns = mutableListOf((iota as PatternIota).pattern.serializeToNBT())
			this.updateRender()
			return true
		} else if (iota.type == HexIotaTypes.LIST) {
			val new = mutableListOf<NbtCompound>()
			(iota as ListIota).list.forEach {
				if (it.type != HexIotaTypes.PATTERN)
					return false
				new.add((it as PatternIota).pattern.serializeToNBT())
			}
			this.patterns = new
			this.updateRender()
			return true
		}
		return false
	}

	override fun writeable() = true
}