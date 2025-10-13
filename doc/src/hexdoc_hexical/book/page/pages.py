from hexdoc.minecraft import LocalizedStr
from hexdoc.patchouli.page import Page
from hexdoc.patchouli.text import FormatTree
from pydantic import ValidationInfo, model_validator

from ..conjure_flora_recipe import ConjureFloraRecipe
from ..transmuting_recipe import TransmutingRecipe
from ...metadata import HexicalContext

class ConjureFloraPage(Page, type="hexcasting:conjure_flora"):
    index: int
    _should_render: bool = False

    @property
    def should_render(self) -> bool:
        return self._should_render

    @property
    def recipe(self) -> ConjureFloraRecipe:
        return self._recipe

    @property
    def text(self):
        return "hexical.page.conjure_flora." + str(self.recipe.id)

    @model_validator(mode="after")
    def test(self, info: ValidationInfo):
        recipes = HexicalContext.of(info).conjure_flora_recipes
        if self.index < len(recipes):
            self._recipe = recipes[self.index]
            self._should_render = True
        return self

class TransmutingPage(Page, type="hexcasting:transmuting"):
    title: LocalizedStr
    recipe: TransmutingRecipe
    text: FormatTree
