package miyucomics.hexical.features.player.fields

import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.api.utils.asDouble
import at.petrak.hexcasting.api.utils.putList
import miyucomics.hexical.features.lesser_sentinels.ServerLesserSentinelPusher
import miyucomics.hexical.features.player.getHexicalPlayerManager
import miyucomics.hexical.features.player.types.PlayerField
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtDouble
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class LesserSentinelField : PlayerField {
	var instances: HashMap<RegistryKey<World>, DimensionalLesserSentinelInstance> = hashMapOf()

	override fun readNbt(compound: NbtCompound) {
		instances.clear()
		if (!compound.contains("lesser_sentinels"))
			return
		compound.getList("lesser_sentinels", NbtElement.COMPOUND_TYPE.toInt()).forEach {
			val instance = DimensionalLesserSentinelInstance.createFromNbt(it.asCompound)
			instances[instance.dimension] = instance
		}
	}

	override fun writeNbt(compound: NbtCompound) {
		compound.putList("lesser_sentinels", NbtList().also {
			instances.values.forEach { instance -> it.add(instance.toNbt()) }
		})
	}

	fun getCurrentInstance(player: ServerPlayerEntity) = instances.getOrPut(player.serverWorld.registryKey) { DimensionalLesserSentinelInstance(mutableListOf(), player.serverWorld.registryKey) }
}

data class DimensionalLesserSentinelInstance(var lesserSentinels: MutableList<Vec3d>, val dimension: RegistryKey<World>) {
	fun toNbt(): NbtCompound {
		val compound = NbtCompound()

		val location = NbtList()
		lesserSentinels.forEach { pos ->
			location.add(NbtDouble.of(pos.x))
			location.add(NbtDouble.of(pos.y))
			location.add(NbtDouble.of(pos.z))
		}

		compound.putString("dimension", dimension.value.toString())
		compound.putList("positional", location)
		return compound
	}

	companion object {
		fun createFromNbt(compound: NbtCompound): DimensionalLesserSentinelInstance {
			val lesserSentinels = mutableListOf<Vec3d>()
			val positions = compound.getList("positional", NbtElement.DOUBLE_TYPE.toInt()).toMutableList()
			while (positions.isNotEmpty())
				lesserSentinels.add(Vec3d(positions.removeFirst().asDouble, positions.removeFirst().asDouble, positions.removeFirst().asDouble))
			return DimensionalLesserSentinelInstance(lesserSentinels, RegistryKey.of(RegistryKeys.WORLD, Identifier(compound.getString("dimension"))))
		}
	}
}

var ServerPlayerEntity.currentLesserSentinels: MutableList<Vec3d>
	get() = this.getHexicalPlayerManager().get(LesserSentinelField::class).getCurrentInstance(this).lesserSentinels
	set(sentinels) { this.getHexicalPlayerManager().get(LesserSentinelField::class).getCurrentInstance(this).lesserSentinels = sentinels }
fun ServerPlayerEntity.syncLesserSentinels() {
	val buf = PacketByteBufs.create()
	val instance = this.currentLesserSentinels
	buf.writeInt(instance.size)
	instance.forEach { pos ->
		buf.writeDouble(pos.x)
		buf.writeDouble(pos.y)
		buf.writeDouble(pos.z)
	}
	ServerPlayNetworking.send(this, ServerLesserSentinelPusher.LESSER_SENTINEL_CHANNEL, buf)
}