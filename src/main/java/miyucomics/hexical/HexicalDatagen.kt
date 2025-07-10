package miyucomics.hexical

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.mod.HexTags
import at.petrak.hexcasting.common.lib.HexItems
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import miyucomics.hexical.Transmutations.createTransmutingPage
import miyucomics.hexical.recipe.TransmutingJsonProvider
import miyucomics.hexical.registry.*
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.advancement.criterion.CriterionConditions
import net.minecraft.advancement.criterion.InventoryChangedCriterion
import net.minecraft.advancement.criterion.InventoryChangedCriterion.Conditions.items
import net.minecraft.data.DataOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.SingleItemRecipeJsonBuilder
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

class HexicalDatagen : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
		Transmutations.init()

		val pack = generator.createPack()
		pack.addProvider(::HexicalAdvancementGenerator)
		pack.addProvider(::HexicalBlockLoottableGenerator)
		pack.addProvider(::HexicalModelGenerator)
		pack.addProvider(::HexicalRecipeGenerator)
		pack.addProvider(::HexicalPatchouliDataProvider)
	}
}

private class HexicalAdvancementGenerator(generator: FabricDataOutput) : FabricAdvancementProvider(generator) {
	override fun generateAdvancement(consumer: Consumer<Advancement>) {
		val root = Advancement.Builder.create()
			.display(ItemStack(HexicalItems.CONJURED_COMPASS_ITEM),
				Text.translatable("advancement.hexical:root.title"),
				Text.translatable("advancement.hexical:root.description"),
				Identifier("minecraft", "textures/block/blackstone.png"),
				AdvancementFrame.TASK, true, true, true)
			.criterion("start_hexcasting", InventoryChangedCriterion.Conditions.items(ItemPredicate.Builder.create().tag(HexTags.Items.GRANTS_ROOT_ADVANCEMENT).build()))
			.build(consumer, "hexical:root")

		fun registerAdvancement(name: String, frame: AdvancementFrame, icon: Item, hidden: Boolean, condition: CriterionConditions, parent: Advancement = root): Advancement {
			return Advancement.Builder.create()
				.parent(parent)
				.display(ItemStack(icon),
					Text.translatable("advancements.hexical.$name.title"),
					Text.translatable("advancements.hexical.$name.description"),
					null, frame, true, true, hidden
				)
				.criterion(name, condition)
				.build(consumer, "hexical:$name")
		}

		registerAdvancement("conjure_cake", AdvancementFrame.CHALLENGE, Items.CAKE, true, ConjureCakeCriterion.Condition())
		registerAdvancement("conjure_hexxy", AdvancementFrame.CHALLENGE, HexItems.SCRYING_LENS, true, HexxyCriterion.Condition())
		registerAdvancement("hallucinate", AdvancementFrame.TASK, Items.WHITE_BANNER, false, HallucinateCriterion.Condition())
		registerAdvancement("diy_conjuring", AdvancementFrame.TASK, Items.SCAFFOLDING, false, DIYCriterion.Condition())
		registerAdvancement("specklike", AdvancementFrame.TASK, Items.BEACON, false, SpecklikeCriterion.Condition())

		val baseLamp = registerAdvancement("acquire_hand_lamp", AdvancementFrame.TASK, HexicalItems.HAND_LAMP_ITEM, false, InventoryChangedCriterion.Conditions.items(HexicalItems.HAND_LAMP_ITEM))
		registerAdvancement("educate_lamp", AdvancementFrame.TASK, Items.ENCHANTED_BOOK, false, EducateGenieCriterion.Condition(), baseLamp)
		registerAdvancement("reload_lamp", AdvancementFrame.TASK, Items.LIGHTNING_ROD, false, ReloadLampCriterion.Condition(), baseLamp)
		registerAdvancement("acquire_arch_lamp", AdvancementFrame.GOAL, HexicalItems.ARCH_LAMP_ITEM, false, InventoryChangedCriterion.Conditions.items(HexicalItems.ARCH_LAMP_ITEM), baseLamp)
	}
}

private class HexicalBlockLoottableGenerator(generator: FabricDataOutput) : FabricBlockLootTableProvider(generator) {
	override fun generate() {
		addDrop(HexicalBlocks.CASTING_CARPET)
		addDrop(HexicalBlocks.HEX_CANDLE_BLOCK, candleDrops(HexicalBlocks.HEX_CANDLE_BLOCK))
		addDrop(HexicalBlocks.PEDESTAL_BLOCK)
		addDrop(HexicalBlocks.PERIWINKLE_FLOWER, flowerbedDrops(HexicalBlocks.PERIWINKLE_FLOWER))
		addDrop(HexicalBlocks.SENTINEL_BED_BLOCK)
	}
}

private class HexicalModelGenerator(generator: FabricDataOutput) : FabricModelProvider(generator) {
	override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
		generator.registerCandle(HexicalBlocks.HEX_CANDLE_BLOCK, HexicalBlocks.HEX_CANDLE_CAKE_BLOCK)
	}

	override fun generateItemModels(generator: ItemModelGenerator) {
		generator.registerCompass(HexicalItems.CONJURED_COMPASS_ITEM)
		for (curio in HexicalItems.CURIOS)
			generator.register(curio, Models.GENERATED)
		for (plushie in HexicalItems.PLUSHIES)
			generator.register(plushie, Models.GENERATED)
	}
}

class HexicalPatchouliDataProvider(val output: FabricDataOutput) : DataProvider {
	override fun run(writer: DataWriter): CompletableFuture<*> {
		val finalJson = JsonObject().apply {
			addProperty("name", "hexical.page.media_jar.title")
			addProperty("icon", "hexical:media_jar")
			addProperty("advancement", "hexcasting:root")
			addProperty("category", "hexcasting:items")
			addProperty("sortnum", 4)
			add("pages", JsonArray().apply {
				add(JsonObject().apply {
					addProperty("type", "patchouli:text")
					addProperty("text", "hexical.page.media_jar.0")
				})
				add(JsonObject().apply {
					addProperty("type", "patchouli:crafting")
					addProperty("recipe", "hexical:media_jar")
					addProperty("text", "hexical.page.media_jar.description")
				})
				add(JsonObject().apply {
					addProperty("type", "patchouli:text")
					addProperty("text", "hexical.page.media_jar.1")
				})
				Transmutations.transmutationRecipePages.forEach { add(it) }
			})
		}

		val path = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "patchouli_books/thehexbook/en_us/entries/items")
		return CompletableFuture.allOf(DataProvider.writeToPath(writer, finalJson, path.resolve(Identifier("hexcasting", "media_jar"), "json")))
	}

	override fun getName(): String = "Patchouli Pages"
}

class HexicalRecipeGenerator(generator: FabricDataOutput) : FabricRecipeProvider(generator) {
	override fun generate(exporter: Consumer<RecipeJsonProvider>) {
		HexicalItems.CURIOS.forEach { curio ->
			SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(HexItems.CHARGED_AMETHYST), RecipeCategory.MISC, curio, 1)
				.criterion(hasItem(HexItems.CHARGED_AMETHYST), conditionsFromItem(HexItems.CHARGED_AMETHYST))
				.offerTo(exporter, Identifier("curio/${Registries.ITEM.getId(curio).path}_from_stonecutting"))
		}

		for (provider in Transmutations.transmutationRecipeJsons)
			exporter.accept(provider)
	}
}

object Transmutations {
	val transmutationRecipeJsons = mutableListOf<TransmutingJsonProvider>()
	val transmutationRecipePages = mutableListOf<JsonObject>()

	fun init() {
		makeTransmutation("alchemists_take_this", Items.COPPER_INGOT, Items.GOLD_INGOT, MediaConstants.SHARD_UNIT)
		makeTransmutation("cry_obsidian", Items.OBSIDIAN, Items.CRYING_OBSIDIAN, MediaConstants.SHARD_UNIT)
		makeTransmutation("uncry_obsidian", Items.CRYING_OBSIDIAN, Items.OBSIDIAN, -2 * MediaConstants.DUST_UNIT)
		makeTransmutation("thoughtknot", Items.STRING, HexItems.THOUGHT_KNOT, (0.75 * MediaConstants.DUST_UNIT).toLong())
		makeTransmutation("unthoughtknot", HexItems.THOUGHT_KNOT, Items.STRING, MediaConstants.DUST_UNIT / -2)
		makeTransmutation("fossil_fuel", Items.CHARCOAL, Items.COAL, MediaConstants.DUST_UNIT / 2)
		makeTransmutation("renewable_fuel", Items.COAL, Items.CHARCOAL, MediaConstants.DUST_UNIT / -4)
	}

	fun makeTransmutation(name: String, original: Item, new: Item, cost: Long) {
		transmutationRecipeJsons.add(TransmutingJsonProvider(HexicalMain.id("transmuting/$name"), Ingredient.ofItems(original), listOf(ItemStack(new)), cost))
		transmutationRecipePages.add(createTransmutingPage(name))
	}

	fun createTransmutingPage(name: String): JsonObject {
		val obj = JsonObject()
		obj.addProperty("type", "hexcasting:transmuting")
		obj.addProperty("recipe", "hexical:transmuting/$name")
		obj.addProperty("title", "hexical.recipe.$name.header")
		obj.addProperty("text", "hexical.recipe.$name.text")
		return obj
	}
}