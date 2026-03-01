package miyucomics.hexical.features.driver_dots

import at.petrak.hexcasting.api.casting.PatternShapeMatch
import at.petrak.hexcasting.api.casting.SpellList
import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.*
import at.petrak.hexcasting.common.casting.PatternRegistryManifest
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexical.features.driver_dots.RecursiveFrame.Companion.wouldBeRecursive
import miyucomics.hexical.features.item_cache.itemCache
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtElement
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Rarity
import net.minecraft.world.World

open class DriverDotItem(val hasKey: Boolean) : Item(Settings().maxCount(1).rarity(Rarity.UNCOMMON)) {
	override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
		val nbt = stack.nbt ?: return
		if (!nbt.contains("program"))
			return
		if (hasKey)
			tooltip.add("hexical.driver_dot.pattern".asTranslatedComponent(PatternIota.display(HexPattern.fromNBT(stack.getCompound("display")!!))).styledWith(Formatting.GRAY))
		tooltip.add("hexical.driver_dot.program".asTranslatedComponent(stack.getList("program", NbtElement.COMPOUND_TYPE.toInt())!!.fold(Text.empty()) { acc, curr -> acc.append(IotaType.getDisplay(curr.asCompound)) }).styledWith(Formatting.GRAY))
	}

	companion object {
		val substitutionRules: List<(CastingVM, PatternIota, SpellContinuation) -> CastResult?> = listOf(
			{ vm, iota, continuation ->
				val env = vm.env
				val pattern = iota.pattern
				val iotaList = (env.castingEntity as ServerPlayerEntity).itemCache().copperDriverDotMacros[pattern.anglesSignature()] ?: return@listOf null
				CastResult(iota, continuation, vm.image.copy(stack = vm.image.stack.plus(iotaList)), listOf(), ResolvedPatternType.EVALUATED, HexEvalSounds.NOTHING)
			},
			{ vm, iota, continuation ->
				val env = vm.env
				val pattern = iota.pattern
				val program = (env.castingEntity as ServerPlayerEntity).itemCache().ironDriverDotMacros[pattern.anglesSignature()] ?: return@listOf null
				constructCastResult(vm, continuation, iota, program)
			},
			{ vm, iota, continuation ->
				val env = vm.env
				val pattern = iota.pattern
				val targetSignature = pattern.anglesSignature()
				val prefix = (env.castingEntity as ServerPlayerEntity).itemCache().goldDriverDotMacros.keys.firstOrNull(targetSignature::startsWith) ?: return@listOf null
				constructCastResult(vm, continuation, iota, (env.castingEntity as ServerPlayerEntity).itemCache().goldDriverDotMacros[prefix]!!, vm.image.stack.plus(iota))
			},
			{ vm, iota, continuation ->
				val program = (vm.env.castingEntity as ServerPlayerEntity).itemCache().netheriteDriverDotProgram ?: return@listOf null
				constructCastResult(vm, continuation, iota, program, vm.image.stack.plus(iota))
			}
		)

		private fun constructCastResult(vm: CastingVM, continuation: SpellContinuation, iota: PatternIota, program: List<Iota>, newStack: List<Iota>? = null): CastResult {
			return CastResult(
				iota,
				continuation
					.pushFrame(RecursiveFrame(iota.pattern.anglesSignature()))
					.pushFrame(FrameEvaluate(SpellList.LList(program), false)),
				if (newStack != null) vm.image.copy(stack = newStack) else vm.image,
				listOf(),
				ResolvedPatternType.EVALUATED,
				HexEvalSounds.NOTHING
			)
		}

		@JvmStatic
		fun applySubstitution(vm: CastingVM, iota: PatternIota, continuation: SpellContinuation): CastResult? {
			if (vm.env !is PlayerBasedCastEnv)
				return null

			val patternTest = PatternRegistryManifest.matchPattern(iota.pattern, vm.env, false)
			if (patternTest !is PatternShapeMatch.Nothing)
				return null
			if (wouldBeRecursive(iota.pattern.anglesSignature(), continuation))
				return null

			substitutionRules.forEach { handler ->
				val result = handler(vm, iota, continuation)
				if (result != null)
					return result
			}

			return null
		}
	}
}