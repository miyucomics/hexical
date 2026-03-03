from hexdoc.minecraft import LocalizedStr
from hexdoc.minecraft.recipe import Recipe
from hexdoc.patchouli.page import Page
from hexdoc.patchouli.text import FormatTree

from hexdoc_hexcasting.book.recipes import BlockState

class FloraRecipe(Recipe, type="hexical:conjure_flora"):
    block: BlockState
    cost: int = 0

    @property
    def cost_translation_key(self) -> str:
        if self.cost == 0:
            return "hexical.recipe.media_free"
        if self.cost > 0:
            return "hexical.recipe.media_cost"
        return "hexical.recipe.media_yield"

    @property
    def cost_translation_number(self) -> float:
        if self.cost == 0:
            return 0
        return round(abs(self.cost) / 10000, 2)

class FloraPage(Page, type="hexcasting:flora"):
    title: LocalizedStr
    recipe: FloraRecipe
    text: FormatTree
