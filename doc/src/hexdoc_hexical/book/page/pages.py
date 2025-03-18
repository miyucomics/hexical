from hexdoc.minecraft import LocalizedStr
from hexdoc.patchouli.page import Page
from hexdoc.patchouli.text import FormatTree

from ..hexical_recipe import TransmutingRecipe

class TransmutingPage(Page, type="hexcasting:transmuting"):
    title: LocalizedStr
    recipe: TransmutingRecipe
    text: FormatTree