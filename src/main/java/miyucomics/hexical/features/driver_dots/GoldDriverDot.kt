package miyucomics.hexical.features.driver_dots

import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.*
import miyucomics.hexical.features.item_cache.itemCache
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtElement
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World

object GoldDriverDot : AbstractDriverDot() {
	override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
		val nbt = stack.nbt ?: return
		if (!nbt.contains("program"))
			return
		tooltip.add("hexical.driver_dot.pattern".asTranslatedComponent(PatternIota.display(HexPattern.fromNBT(stack.getCompound("display")!!))).styledWith(Formatting.GRAY))
		tooltip.add("hexical.driver_dot.program".asTranslatedComponent(stack.getList("program", NbtElement.COMPOUND_TYPE.toInt())!!.fold(Text.empty()) { acc, curr -> acc.append(IotaType.getDisplay(curr.asCompound)) }).styledWith(Formatting.GRAY))
	}

	@JvmStatic
	fun handleGoldDriverDot(vm: CastingVM, iota: PatternIota, continuation: SpellContinuation): CastResult? {
		val env = vm.env
		val pattern = iota.pattern
		if (!canTrigger(env, pattern, continuation))
			return null

		val targetSignature = pattern.anglesSignature()
		val prefix = (env.caster as ServerPlayerEntity).itemCache().goldDriverDotMacros.keys.firstOrNull(targetSignature::startsWith) ?: return null

		return constructCastResult(vm, continuation, iota, (env.caster as ServerPlayerEntity).itemCache().goldDriverDotMacros[prefix]!!, vm.image.stack.plus(iota))
	}
}