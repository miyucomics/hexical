package miyucomics.hexical.features.mage_blocks

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.mage_blocks.modifiers.*
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry

object MageBlockModifierRegistry : InitHook() {
	private val MODIFIER_REGISTRY_KEY: RegistryKey<Registry<MageBlockModifierType<*>>> = RegistryKey.ofRegistry(HexicalMain.id("mage_block_modifier"))
	val MODIFIER_REGISTRY: SimpleRegistry<MageBlockModifierType<*>> = FabricRegistryBuilder.createSimple(MODIFIER_REGISTRY_KEY).attribute(RegistryAttribute.MODDED).buildAndRegister()

	fun register(type: MageBlockModifierType<*>) {
		Registry.register(MODIFIER_REGISTRY, type.id, type)
	}

	override fun init() {
		register(BouncyModifier.TYPE)
		register(LifespanModifier.TYPE)
		register(RedstoneModifier.TYPE)
		register(ReplaceableModifier.TYPE)
		register(VolatileModifier.TYPE)
	}
}