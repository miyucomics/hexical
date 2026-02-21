package miyucomics.hexical.datagen.generators

import miyucomics.hexical.datagen.providers.DyeingProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import java.util.concurrent.CompletableFuture

class HexicalDyeingGenerator(val output: FabricDataOutput) : DataProvider {
	override fun getName() = "Hexical Dyeing"
	override fun run(writer: DataWriter): CompletableFuture<*> =
		CompletableFuture.allOf(*DyeingProvider.tasks.map { it(writer, output) }.toTypedArray())
}