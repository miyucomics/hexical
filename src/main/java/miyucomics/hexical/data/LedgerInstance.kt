package miyucomics.hexical.data

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.putList
import miyucomics.hexical.utils.RingBuffer
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import net.minecraft.network.PacketByteBuf
import net.minecraft.text.Text

class LedgerInstance {
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

	fun toNbt(): NbtCompound {
		val tag = NbtCompound()

		val nbtLedger = NbtList()
		patterns.buffer().forEach { pattern -> nbtLedger.add(pattern.serializeToNBT()) }
		tag.putList("ledger", nbtLedger)

		val nbtStack = NbtList()
		stack.buffer().forEach { iota -> nbtStack.add(NbtString.of(Text.Serializer.toJson(iota))) }
		tag.putList("stack", nbtStack)

		tag.putString("mishap", Text.Serializer.toJson(mishap))

		return tag
	}

	fun toPacket(): PacketByteBuf {
		val buf = PacketByteBufs.create()
		buf.writeNbt(this.toNbt())
		return buf
	}

	companion object {
		fun createFromNbt(tag: NbtCompound): LedgerInstance {
			val state = LedgerInstance()
			tag.getList("ledger", NbtCompound.COMPOUND_TYPE.toInt()).forEach { pattern -> state.patterns.add(HexPattern.fromNBT(pattern as NbtCompound)) }
			tag.getList("stack", NbtCompound.STRING_TYPE.toInt()).forEach { iota -> state.stack.add(Text.Serializer.fromJson((iota as NbtString).asString())!!) }
			state.mishap = Text.Serializer.fromJson(tag.getString("mishap"))!!
			return state
		}
	}
}