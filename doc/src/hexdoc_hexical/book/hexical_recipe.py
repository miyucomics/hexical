from typing import List

from hexdoc.minecraft.assets import ItemWithTexture
from hexdoc.minecraft.recipe import ItemIngredient, Recipe
from hexdoc.model import HexdocModel

class ItemIngredientWithCount(HexdocModel):
    ingredient: ItemIngredient
    count: int = 1

class TransmutingRecipe(Recipe, type="hexcasting:transmuting"):
    priority: int = 0
    mediaCost: int = 10000
    ingredients: ItemIngredientWithCount
    result: list[ItemWithTexture]