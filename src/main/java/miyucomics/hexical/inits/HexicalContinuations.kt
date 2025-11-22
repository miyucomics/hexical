package miyucomics.hexical.inits

import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.misc.RecursiveFrame
import net.minecraft.registry.Registry

object HexicalContinuations {
	fun init() {
		Registry.register(IXplatAbstractions.INSTANCE.continuationTypeRegistry, HexicalMain.id("recursive"), RecursiveFrame.TYPE)
	}
}