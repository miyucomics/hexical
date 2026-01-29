package miyucomics.hexical.inits

import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.driver_dots.RecursiveFrame
import net.minecraft.registry.Registry

object HexicalContinuations {
	fun init() {
		Registry.register(IXplatAbstractions.INSTANCE.continuationTypeRegistry, HexicalMain.id("recursive"), RecursiveFrame.TYPE)
	}
}