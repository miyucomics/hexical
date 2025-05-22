package miyucomics.hexical.entities

import miyucomics.hexical.registry.HexicalEntities
import net.minecraft.entity.*
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.world.World
import java.util.*

class ShieldEntity(entityType: EntityType<ShieldEntity>, world: World) : Entity(entityType, world) {
	private var conjurerUUID: UUID? = null
	private var conjurer: PlayerEntity? = null

	constructor(world: World, x: Double, y: Double, z: Double) : this(HexicalEntities.SHIELD_ENTITY, world) {
		this.setPosition(x, y, z)
	}

	override fun readCustomDataFromNbt(nbt: NbtCompound) {
		if (nbt.containsUuid("conjurer"))
			this.conjurerUUID = nbt.getUuid("conjurer")
		else {
			this.conjurer = null
			this.conjurerUUID = null
		}
	}

	override fun writeCustomDataToNbt(nbt: NbtCompound) {
		if (this.conjurerUUID != null)
			nbt.putUuid("conjurer", this.conjurerUUID)
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
		this.conjurer = this.world.getPlayerByUuid(conjurerUUID)
		if (this.conjurer != null)
			return conjurer
		return null
	}

	override fun getEyeHeight(pose: EntityPose, dimensions: EntityDimensions) = 0.5f
	override fun createSpawnPacket() = EntitySpawnS2CPacket(this)

	override fun initDataTracker() {
		this.dataTracker.startTracking(directionDataTracker, 0)
	}

	companion object {
		private val directionDataTracker: TrackedData<Int> = DataTracker.registerData(ShieldEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
	}
}