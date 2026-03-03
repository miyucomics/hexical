from hexdoc.core import ResourceLocation
from hexdoc.minecraft.recipe import ItemResult
from hexdoc.patchouli.page import Page
from hexdoc.patchouli.text import FormatTree
from pydantic import model_validator
from typing import Any

class DyeingResult(ItemResult):
    nbt: Any | None = None

    @model_validator(mode="before")
    @classmethod
    def _validate_string(cls, value: Any):
        match value:
            case str() | ResourceLocation():
                return {"item": value}
            case _:
                return value

class DyeingPage(Page, type="hexical:dyeing"):
    text: FormatTree

    uncolored: DyeingResult | None = None
    black: DyeingResult | None = None
    blue: DyeingResult | None = None
    brown: DyeingResult | None = None
    cyan: DyeingResult | None = None
    gray: DyeingResult | None = None
    green: DyeingResult | None = None
    light_blue: DyeingResult | None = None
    light_gray: DyeingResult | None = None
    lime: DyeingResult | None = None
    magenta: DyeingResult | None = None
    orange: DyeingResult | None = None
    pink: DyeingResult | None = None
    purple: DyeingResult | None = None
    red: DyeingResult | None = None
    white: DyeingResult | None = None
    yellow: DyeingResult | None = None

    @property
    def colored_items(self) -> dict[str, DyeingResult]:
        naive = [self.uncolored, self.black, self.blue, self.brown, self.cyan, self.gray, self.green, self.light_blue, self.light_gray, self.lime, self.magenta, self.orange, self.pink, self.purple, self.red, self.white, self.yellow]
        return [result for result in naive if result is not None]
