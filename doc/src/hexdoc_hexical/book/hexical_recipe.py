from hexdoc.minecraft.assets import ItemWithTexture
from hexdoc.minecraft.recipe import ItemIngredientList, Recipe

class TransmutingRecipe(Recipe, type="hexcasting:transmuting"):
    cost: int = 0
    input: ItemIngredientList
    output: list[ItemWithTexture]
