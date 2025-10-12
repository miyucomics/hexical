from hexdoc.minecraft import LocalizedStr
from hexdoc.patchouli.page import Page
from hexdoc.patchouli.text import FormatTree

from ..transmuting_recipe import TransmutingRecipe

class ConjureFloraPage(Page, type="hexcasting:conjure_flora"):
    index: int

class TransmutingPage(Page, type="hexcasting:transmuting"):
    title: LocalizedStr
    recipe: TransmutingRecipe
    text: FormatTree
