package miyucomics.hexical.casting.frames

import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.eval.vm.ContinuationFrame
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.utils.getList
import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld

data class NephthysFrame(val original: List<Iota>) : ContinuationFrame {
	override val type: ContinuationFrame.Type<*> = TYPE

	override fun breakDownwards(stack: List<Iota>): Pair<Boolean, List<Iota>> {
		return true to stack
	}

	override fun evaluate(continuation: SpellContinuation, world: ServerWorld, vm: CastingVM): CastResult {
		return CastResult(
			ListIota(original),
			continuation,
			vm.image.withResetEscape().copy(stack = vm.image.stack.toMutableList().plus(original)),
			listOf(),
			ResolvedPatternType.EVALUATED,
			HexEvalSounds.HERMES
		)
	}

	override fun serializeToNBT(): NbtCompound {
		val compound = NbtCompound()
		compound.put("original", original.serializeToNBT())
		return compound
	}

	override fun size() = original.size

	companion object {
		val TYPE: ContinuationFrame.Type<NephthysFrame> = object : ContinuationFrame.Type<NephthysFrame> {
			override fun deserializeFromNBT(tag: NbtCompound, world: ServerWorld) =
				NephthysFrame(HexIotaTypes.LIST.deserialize(tag.getList("original", NbtCompound.COMPOUND_TYPE), world)!!.list.toList())
		}
	}
}