package miyucomics.hexical.features.scarabs

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
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.item.IotaHolderItem
import at.petrak.hexcasting.api.utils.*
import at.petrak.hexcasting.common.casting.PatternRegistryManifest
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexical.features.item_cache.itemCache
import miyucomics.hexical.inits.HexicalSounds
import miyucomics.hexical.misc.HexSerialization
import miyucomics.hexical.misc.RecursiveFrame
import miyucomics.hexical.misc.RecursiveFrame.Companion.wouldBeRecursive
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtElement
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.Rarity
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

object ScarabBeetleItem : Item(Settings().maxCount(1).rarity(Rarity.UNCOMMON)), IotaHolderItem {
	override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
		val stack = user.getStackInHand(hand)
		val nbt = stack.orCreateNbt
		nbt.putBoolean("active", !nbt.getBoolean("active"))
		if (world.isClient)
			world.playSound(user.x, user.y, user.z, HexicalSounds.SCARAB_CHIRPS, SoundCategory.MASTER, 1f, 1f, true)
		return TypedActionResult.success(stack)
	}

	override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
		val nbt = stack.nbt ?: return
		if (!nbt.contains("hex"))
			return
		tooltip.add("hexical.scarab.hex".asTranslatedComponent(stack.getList("hex", NbtElement.COMPOUND_TYPE.toInt())!!.fold(Text.empty()) { acc, curr -> acc.append(IotaType.getDisplay(curr.asCompound)) }).styledWith(Formatting.GRAY))
	}

	override fun readIotaTag(stack: ItemStack) = null
	override fun writeable(stack: ItemStack) = true
	override fun canWrite(stack: ItemStack, iota: Iota?) = iota == null || iota is ListIota
	override fun writeDatum(stack: ItemStack, iota: Iota?) {
		if (iota == null)
			stack.remove("hex")
		else
			stack.putList("hex", HexSerialization.serializeHex((iota as ListIota).list.toList()))
	}

	@JvmStatic
	fun handleScarab(vm: CastingVM, iota: PatternIota, continuation: SpellContinuation): CastResult? {
		val env = vm.env
		if (env !is PlayerBasedCastEnv)
			return null

		val pattern = iota.pattern
		val patternTest = PatternRegistryManifest.matchPattern(pattern, env, false)
		if (patternTest !is PatternShapeMatch.Nothing)
			return null
		if (wouldBeRecursive(pattern.anglesSignature(), continuation))
			return null

		val program = (env.caster as ServerPlayerEntity).itemCache().scarabProgram ?: return null
		val newStack = vm.image.stack.toMutableList()
		newStack.add(iota)

		return CastResult(
			iota,
			continuation
				.pushFrame(RecursiveFrame(pattern.anglesSignature()))
				.pushFrame(FrameEvaluate(SpellList.LList(program), false)),
			vm.image.copy(stack = newStack),
			listOf(),
			ResolvedPatternType.EVALUATED,
			HexEvalSounds.NOTHING
		)
	}
}