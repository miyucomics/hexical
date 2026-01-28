package miyucomics.hexical.features.driver_dots

import at.petrak.hexcasting.api.casting.PatternShapeMatch
import at.petrak.hexcasting.api.casting.SpellList
import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
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
import miyucomics.hexical.misc.RecursiveFrame
import miyucomics.hexical.misc.RecursiveFrame.Companion.wouldBeRecursive
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtElement
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Rarity
import net.minecraft.world.World

open class AbstractDriverDot : Item(Settings().maxCount(1).rarity(Rarity.UNCOMMON)) {
	override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
		val nbt = stack.nbt ?: return
		if (!nbt.contains("program"))
			return
		tooltip.add("hexical.driver_dot.pattern".asTranslatedComponent(PatternIota.display(HexPattern.fromNBT(stack.getCompound("display")!!))).styledWith(Formatting.GRAY))
		tooltip.add("hexical.driver_dot.program".asTranslatedComponent(stack.getList("program", NbtElement.COMPOUND_TYPE.toInt())!!.fold(Text.empty()) { acc, curr -> acc.append(IotaType.getDisplay(curr.asCompound)) }).styledWith(Formatting.GRAY))
	}

	fun canTrigger(env: CastingEnvironment, pattern: HexPattern, continuation: SpellContinuation): Boolean {
		if (env !is PlayerBasedCastEnv)
			return false

		val patternTest = PatternRegistryManifest.matchPattern(pattern, env, false)
		if (patternTest !is PatternShapeMatch.Nothing)
			return false
		if (wouldBeRecursive(pattern.anglesSignature(), continuation))
			return false

		return true
	}

	fun constructCastResult(vm: CastingVM, continuation: SpellContinuation, iota: PatternIota, program: List<Iota>, newStack: List<Iota>? = null): CastResult {
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
}