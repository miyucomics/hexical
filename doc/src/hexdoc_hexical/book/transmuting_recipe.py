from hexdoc.core import ResourceLocation
from hexdoc.minecraft.recipe import ItemIngredient, Recipe, ItemResult
from pydantic import BeforeValidator, model_validator
from typing import Annotated, Any

class TransmutingResult(ItemResult):
    nbt: Any | None = None

    @model_validator(mode="before")
    @classmethod
    def _validate_string(cls, value: Any):
        match value:
            case str() | ResourceLocation():
                return {"item": value}
            case _:
                return value

def _validate_single_item_to_list(value: Any) -> list[Any]:
    match value:
        case list():
            return value  # pyright: ignore[reportUnknownVariableType]
        case _:
            return [value]

TransmutingResultList = Annotated[
    list[TransmutingResult],
    BeforeValidator(_validate_single_item_to_list),
]

class TransmutingRecipe(Recipe, type="hexical:transmuting"):
    input: ItemIngredient
    output: TransmutingResultList
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
