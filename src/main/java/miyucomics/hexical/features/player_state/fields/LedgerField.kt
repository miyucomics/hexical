package miyucomics.hexical.features.player_state.fields

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PackagedItemCastEnv
import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.putCompound
import at.petrak.hexcasting.api.utils.putList
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.casting.environments.HandLampCastEnv
import miyucomics.hexical.client.ClientStorage
import miyucomics.hexical.features.player_state.PlayerField
import miyucomics.hexical.features.player_state.getHexicalPlayerState
import miyucomics.hexical.utils.RingBuffer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class LedgerField : PlayerField {
	var patterns: RingBuffer<HexPattern> = RingBuffer(32)
	var stack: RingBuffer<Text> = RingBuffer(16)
	var mishap: Text = Text.empty()
	var active = true

	fun saveMishap(text: Text) {
		mishap = text
	}

	fun pushPattern(pattern: HexPattern) {
		patterns.add(pattern)
	}

	fun saveStack(iotas: List<Iota>) {
		stack.clear()
		iotas.forEach { iota -> stack.add(iota.display()) }
	}

	override fun readNbt(compound: NbtCompound) {
		if (!compound.contains("ledger"))
			return
		fromNbt(compound.getCompound("ledger"))
	}

	override fun writeNbt(compound: NbtCompound) {
		compound.putCompound("ledger", toNbt())
	}

	fun fromNbt(ledger: NbtCompound) {
		ledger.getList("patterns", NbtCompound.COMPOUND_TYPE.toInt()).forEach { pattern -> patterns.add(HexPattern.fromNBT(pattern as NbtCompound)) }
		ledger.getList("stack", NbtCompound.STRING_TYPE.toInt()).forEach { iota -> stack.add(Text.Serializer.fromJson((iota as NbtString).asString())!!) }
		this.mishap = Text.Serializer.fromJson(ledger.getString("mishap")) ?: Text.empty()
	}

	fun toNbt(): NbtCompound {
		return NbtCompound().also { compound ->
			compound.putList("patterns", NbtList().also { patterns.buffer().forEach { pattern -> it.add(pattern.serializeToNBT()) } })
			compound.putList("stack", NbtList().also { stack.buffer().forEach { iota -> it.add(NbtString.of(Text.Serializer.toJson(iota))) } })
			compound.putString("mishap", Text.Serializer.toJson(mishap))
		}
	}

	fun toPacket(): PacketByteBuf {
		val buf = PacketByteBufs.create()
		buf.writeNbt(this.toNbt())
		return buf
	}

	companion object {
		val LEDGER_CHANNEL: Identifier = HexicalMain.id("ledger")

		@JvmStatic
		fun isEnvCompatible(env: CastingEnvironment) = env is StaffCastEnv || env is PackagedItemCastEnv

		fun registerClientCallbacks() {
			ClientPlayNetworking.registerGlobalReceiver(LEDGER_CHANNEL) { _, _, packet, _ ->
				ClientStorage.ledger = LedgerField().also { it.fromNbt(packet.readNbt()!!) }
			}
		}
	}
}

fun PlayerEntity.getLedger() = this.getHexicalPlayerState().get(LedgerField::class)
fun ServerPlayerEntity.syncLedger() { ServerPlayNetworking.send(this, LedgerField.LEDGER_CHANNEL, this.getLedger().toPacket()) }