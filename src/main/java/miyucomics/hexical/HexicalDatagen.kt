package miyucomics.hexical

import at.petrak.hexcasting.api.mod.HexTags
import at.petrak.hexcasting.common.lib.HexItems
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
import java.util.function.Consumer

class HexicalDatagen : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
		val pack = generator.createPack()
		pack.addProvider(::HexicalAdvancementGenerator)
		pack.addProvider(::HexicalBlockLoottableGenerator)
		pack.addProvider(::HexicalModelGenerator)
		pack.addProvider(::HexicalRecipeGenerator)
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

class HexicalRecipeGenerator(generator: FabricDataOutput) : FabricRecipeProvider(generator) {
	override fun generate(exporter: Consumer<RecipeJsonProvider>) {
		HexicalItems.CURIOS.forEach { curio ->
			SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(HexItems.CHARGED_AMETHYST), RecipeCategory.MISC, curio, 1)
				.criterion(hasItem(HexItems.CHARGED_AMETHYST), conditionsFromItem(HexItems.CHARGED_AMETHYST))
				.offerTo(exporter, Identifier("items/" + Registries.ITEM.getId(curio).path + "_from_stonecutting"))
		}
	}
}