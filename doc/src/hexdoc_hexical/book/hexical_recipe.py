from typing import Annotated, Any
from hexdoc.core import ResourceLocation
from hexdoc.minecraft.recipe import ItemIngredientList, Recipe, ItemResult
from pydantic import BeforeValidator, model_validator


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
    input: ItemIngredientList
    output: TransmutingResultList
    cost: int = 0
