package miyucomics.hexical.features.scarabs

import at.petrak.hexcasting.api.casting.PatternShapeMatch
import at.petrak.hexcasting.api.casting.SpellList
import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.utils.getList
import at.petrak.hexcasting.common.casting.PatternRegistryManifest
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.inits.HexicalItems
import miyucomics.hexical.misc.HexItemsFinder
import miyucomics.hexical.misc.HexSerialization
import miyucomics.hexical.misc.InitHook
import net.minecraft.nbt.NbtElement
import net.minecraft.registry.Registry
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld

object ScarabHandler : InitHook() {
	override fun init() {
		Registry.register(IXplatAbstractions.INSTANCE.continuationTypeRegistry, HexicalMain.id("scarab"), ScarabFrame.TYPE)
	}

	@JvmStatic
	fun handleScarab(vm: CastingVM, iota: PatternIota, continuation: SpellContinuation, world: ServerWorld): CastResult? {
		val env = vm.env
		if (env !is PlayerBasedCastEnv)
			return null

		val pattern = iota.pattern
		val patternTest = PatternRegistryManifest.matchPattern(pattern, env, false)
		if (patternTest !is PatternShapeMatch.Nothing)
			return null

		if (wouldBeRecursive(pattern.anglesSignature(), continuation))
			return null
		val program = if (vm.image.userData.contains("scarab_hex"))
			HexSerialization.deserializeHex(vm.image.userData.getList("scarab_hex", NbtElement.COMPOUND_TYPE.toInt()), world)
		else {
			val scarab = HexItemsFinder.getMatchingItem(env.castingEntity!! as ServerPlayerEntity) { stack -> stack.isOf(HexicalItems.SCARAB_BEETLE_ITEM) && stack.hasNbt() && stack.nbt!!.getBoolean("active") } ?: return null
			val program = scarab.getList("hex", NbtElement.COMPOUND_TYPE.toInt()) ?: return null
			vm.image.userData.put("scarab_hex", program)
			HexSerialization.deserializeHex(program, world)
		}

		val newStack = vm.image.stack.toMutableList()
		newStack.add(iota)

		return CastResult(
			iota,
			continuation
				.pushFrame(ScarabFrame(pattern.anglesSignature()))
				.pushFrame(FrameEvaluate(SpellList.LList(program), false)),
			vm.image.copy(stack = newStack),
			listOf(),
			ResolvedPatternType.EVALUATED,
			HexEvalSounds.NOTHING
		)
	}

	private fun wouldBeRecursive(pattern: String, continuation: SpellContinuation): Boolean {
		var cont = continuation
		while (cont is SpellContinuation.NotDone) {
			if (cont.frame is ScarabFrame && (cont.frame as ScarabFrame).signature == pattern)
				return true
			cont = cont.next
		}
		return false
	}
}