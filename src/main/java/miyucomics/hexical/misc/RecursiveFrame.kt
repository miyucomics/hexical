package miyucomics.hexical.misc

import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.eval.vm.ContinuationFrame
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld

// continuation frame used by scarabs and driver dots to prevent recursive macros
data class RecursiveFrame(val signature: String) : ContinuationFrame {
	override val type: ContinuationFrame.Type<*> = TYPE
	override fun evaluate(continuation: SpellContinuation, level: ServerWorld, harness: CastingVM) = CastResult(NullIota(), continuation, null, listOf(), ResolvedPatternType.EVALUATED, HexEvalSounds.NOTHING)
	override fun serializeToNBT() = NbtCompound().apply { putString("signature", signature) }
	override fun breakDownwards(stack: List<Iota>) = true to stack
	override fun size() = 0

	companion object {
		val TYPE: ContinuationFrame.Type<RecursiveFrame> = object : ContinuationFrame.Type<RecursiveFrame> {
			override fun deserializeFromNBT(compound: NbtCompound, world: ServerWorld) = RecursiveFrame(compound.getString("signature"))
		}

		fun wouldBeRecursive(pattern: String, continuation: SpellContinuation): Boolean {
			var cont = continuation
			while (cont is SpellContinuation.NotDone) {
				if (cont.frame is RecursiveFrame && (cont.frame as RecursiveFrame).signature == pattern)
					return true
				cont = cont.next
			}
			return false
		}
	}
}