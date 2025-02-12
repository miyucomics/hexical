from hexdoc.patchouli.page import PageWithText

from ..hexical_recipe import TransmutingRecipe

class TransmutingPage(PageWithText, type="hexcasting:transmuting"):
    recipe: TransmutingRecipe