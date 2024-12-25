package miyucomics.hexical.casting.frames

import at.petrak.hexcasting.api.casting.SpellList
import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType
import at.petrak.hexcasting.api.casting.eval.vm.*
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.utils.getList
import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld

data class SisyphusFrame(val code: SpellList) : ContinuationFrame {
	override val type: ContinuationFrame.Type<*> = TYPE

	override fun breakDownwards(stack: List<Iota>): Pair<Boolean, List<Iota>> {
		return true to stack
	}

	override fun evaluate(continuation: SpellContinuation, world: ServerWorld, vm: CastingVM) = CastResult(
			ListIota(code),
			continuation
				.pushFrame(SisyphusFrame(code))
				.pushFrame(FrameEvaluate(code, true)),
			vm.image.withResetEscape(),
			listOf(),
			ResolvedPatternType.EVALUATED,
			HexEvalSounds.THOTH
		)

	override fun serializeToNBT(): NbtCompound {
		val compound = NbtCompound()
		compound.put("code", code.serializeToNBT())
		return compound
	}

	override fun size() = code.size()

	companion object {
		val TYPE: ContinuationFrame.Type<SisyphusFrame> = object : ContinuationFrame.Type<SisyphusFrame> {
			override fun deserializeFromNBT(tag: NbtCompound, world: ServerWorld) =
				SisyphusFrame(HexIotaTypes.LIST.deserialize(tag.getList("code", NbtCompound.COMPOUND_TYPE), world)!!.list)
		}
	}
}