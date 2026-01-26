package miyucomics.hexical.features.animated_scrolls

import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.serializeToNBT
import miyucomics.hexical.inits.HexicalEntities
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.misc.PatternUtils
import net.minecraft.block.AbstractRedstoneGateBlock
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityPose
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.decoration.AbstractDecorationEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import net.minecraft.world.GameRules
import net.minecraft.world.World

class AnimatedScrollEntity(entityType: EntityType<AnimatedScrollEntity>, world: World) : AbstractDecorationEntity(entityType, world) {
	var pattern: HexPattern? = null
	var cachedVerts: List<Vec2f> = listOf()
	lateinit var scroll: ItemStack

	constructor(world: World) : this(HexicalEntities.ANIMATED_SCROLL_ENTITY, world)

	constructor(world: World, position: BlockPos, dir: Direction, size: Int, pattern: HexPattern?, scroll: ItemStack) : this(world) {
		this.attachmentPos = position
		this.dataTracker.set(sizeDataTracker, size)
		this.scroll = scroll
		setPatternAndUpdate(pattern)
		setFacing(dir)
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

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		nbt.putInt("direction", facing.id)
		nbt.putInt("state", this.dataTracker.get(stateDataTracker))
		nbt.putInt("color", this.dataTracker.get(colorDataTracker))
		nbt.putBoolean("glow", this.dataTracker.get(glowDataTracker))
		nbt.putInt("size", this.dataTracker.get(sizeDataTracker))
		nbt.putCompound("scroll", this.scroll.serializeToNBT())
		this.pattern?.also { nbt.putCompound("pattern", it.serializeToNBT()) }

		super.writeCustomDataToNbt(nbt)
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		this.facing = Direction.byId(nbt.getInt("direction"))
		this.dataTracker.set(stateDataTracker, nbt.getInt("state"))
		this.dataTracker.set(glowDataTracker, nbt.getBoolean("glow"))
		this.dataTracker.set(colorDataTracker, nbt.getInt("color"))
		this.dataTracker.set(sizeDataTracker, nbt.getInt("size"))
		this.scroll = ItemStack.fromNbt(nbt.getCompound("scroll"))
		this.pattern = nbt.getCompound("pattern")?.let(HexPattern::fromNBT)

		setFacing(this.facing)
		updateAttachmentPosition()
		super.readCustomDataFromNbt(nbt)
	}

	override fun onPlace() = playSound(SoundEvents.ENTITY_PAINTING_PLACE, 1.0F, 1.0F)
	override fun onBreak(entity: Entity?) {
		this.playSound(SoundEvents.ENTITY_PAINTING_BREAK, 1.0f, 1.0f)
		if (this.world.gameRules.getBoolean(GameRules.DO_ENTITY_DROPS))
			this.dropStack(this.scroll)
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

	fun setState(state: Int) {
		scroll.orCreateNbt.putInt("state", state)
		this.dataTracker.set(stateDataTracker, state)
	}
	fun setColor(color: Int) {
		scroll.orCreateNbt.putInt("color", color)
		this.dataTracker.set(colorDataTracker, color)
	}
	fun toggleGlow() {
		this.dataTracker.set(glowDataTracker, !this.dataTracker.get(glowDataTracker))
		scroll.orCreateNbt.putBoolean("glow", this.dataTracker.get(glowDataTracker))
	}
	fun setPatternAndUpdate(pattern: HexPattern?) {
		this.pattern = pattern
		this.dataTracker.set(patternDataTracker, pattern?.serializeToNBT() ?: NbtCompound())
	}

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
				this.cachedVerts = if (nbt.contains(HexPattern.TAG_START_DIR)) PatternUtils.getNormalizedStrokes(HexPattern.fromNBT(nbt), true) else listOf()
			}
		}
	}

	companion object {
		private val patternDataTracker: TrackedData<NbtCompound> = DataTracker.registerData(AnimatedScrollEntity::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
		val glowDataTracker: TrackedData<Boolean> = DataTracker.registerData(AnimatedScrollEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
		val colorDataTracker: TrackedData<Int> = DataTracker.registerData(AnimatedScrollEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
		val sizeDataTracker: TrackedData<Int> = DataTracker.registerData(AnimatedScrollEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
		val stateDataTracker: TrackedData<Int> = DataTracker.registerData(AnimatedScrollEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
	}
}