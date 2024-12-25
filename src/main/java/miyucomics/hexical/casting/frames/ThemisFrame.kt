package miyucomics.hexical.casting.frames

import at.petrak.hexcasting.api.casting.SpellList
import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType
import at.petrak.hexcasting.api.casting.eval.vm.*
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.utils.*
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.hexical.casting.mishaps.ThemisMishap
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtDouble
import net.minecraft.nbt.NbtList
import net.minecraft.server.world.ServerWorld

data class ThemisFrame(val data: SpellList, val code: SpellList, val baseStack: List<Iota>?, val seen: MutableList<Iota>, val priorities: MutableList<Double>) : ContinuationFrame {
	override val type: ContinuationFrame.Type<*> = TYPE

	override fun breakDownwards(stack: List<Iota>): Pair<Boolean, List<Iota>> {
		val newStack = baseStack?.toMutableList() ?: mutableListOf()
		val zipped = seen.zip(priorities)
		val sorted = zipped.sortedBy { it.second }
		newStack.add(ListIota(sorted.map { it.first }))
		return true to newStack
	}

	override fun evaluate(continuation: SpellContinuation, level: ServerWorld, vm: CastingVM): CastResult {
		val stack = if (baseStack == null) {
			vm.image.stack.toList()
		} else {
			val top = vm.image.stack.lastOrNull() ?: throw ThemisMishap()
			if (top !is DoubleIota)
				throw ThemisMishap()
			priorities.add(top.double)
			baseStack
		}

		val (stackTop, newImage, newCont) = if (data.nonEmpty) {
			val cont2 = continuation
				.pushFrame(ThemisFrame(data.cdr, code, stack, seen, priorities))
				.pushFrame(FrameEvaluate(code, true))
			seen.add(data.car)
			Triple(data.car, vm.image.withUsedOp(), cont2)
		} else {
			val zipped = seen.zip(priorities)
			val sorted = zipped.sortedBy { it.second }
			Triple(ListIota(sorted.map { it.first }), vm.image, continuation)
		}

		val newStack = stack.toMutableList()
		newStack.add(stackTop)
		return CastResult(
			ListIota(code),
			newCont,
			newImage.withResetEscape().copy(stack = newStack),
			listOf(),
			ResolvedPatternType.EVALUATED,
			HexEvalSounds.THOTH,
		)
	}

	override fun serializeToNBT(): NbtCompound {
		val compound = NbtCompound()
		compound.put("data", data.serializeToNBT())
		compound.put("code", code.serializeToNBT())
		if (baseStack != null)
			compound.put("base", baseStack.serializeToNBT())
		compound.put("seen", seen.serializeToNBT())
		val nbtPriorities = NbtList()
		priorities.forEach { num -> nbtPriorities.add(NbtDouble.of(num)) }
		compound.putList("priorities", nbtPriorities)
		return compound
	}

	override fun size() = data.size() + code.size() + seen.size + (baseStack?.size ?: 0)

	companion object {
		@JvmField
		val TYPE: ContinuationFrame.Type<ThemisFrame> = object : ContinuationFrame.Type<ThemisFrame> {
			override fun deserializeFromNBT(tag: NbtCompound, world: ServerWorld): ThemisFrame {
				val priorities = mutableListOf<Double>()
				tag.getList("priorities", NbtCompound.DOUBLE_TYPE).forEach { priority -> priorities.add((priority as NbtDouble).doubleValue())}

				return ThemisFrame(
					HexIotaTypes.LIST.deserialize(tag.getList("data", NbtCompound.COMPOUND_TYPE), world)!!.list,
					HexIotaTypes.LIST.deserialize(tag.getList("code", NbtCompound.COMPOUND_TYPE), world)!!.list,
					if (tag.hasList("base", NbtCompound.COMPOUND_TYPE))
						HexIotaTypes.LIST.deserialize(tag.getList("base", NbtCompound.COMPOUND_TYPE), world)!!.list.toList()
					else
						null,
					HexIotaTypes.LIST.deserialize(tag.getList("seen", NbtCompound.COMPOUND_TYPE), world)!!.list.toMutableList(),
					priorities
				)
			}
		}
	}
}