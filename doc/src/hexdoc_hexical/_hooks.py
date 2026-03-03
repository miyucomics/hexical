from importlib.resources import Package

import hexdoc_hexical
from hexdoc.plugin import (HookReturn, ModPlugin, ModPluginImpl, ModPluginWithBook, hookimpl)
from typing_extensions import override

from .__gradle_version__ import FULL_VERSION, GRADLE_VERSION
from .__version__ import PY_VERSION
from .book import dyeing_page, flora_page, transmuting_page

class HexicalPlugin(ModPluginImpl):
    @staticmethod
    @hookimpl
    def hexdoc_mod_plugin(branch: str) -> ModPlugin:
        return HexicalModPlugin(branch=branch)

    @staticmethod
    @hookimpl
    def hexdoc_load_tagged_unions() -> HookReturn[Package]:
        return [dyeing_page, flora_page, transmuting_page]

class HexicalModPlugin(ModPluginWithBook):
    @property
    @override
    def modid(self) -> str:
        return "hexical"

    @property
    @override
    def full_version(self) -> str:
        return FULL_VERSION

    @property
    @override
    def mod_version(self) -> str:
        return GRADLE_VERSION

    @property
    @override
    def plugin_version(self) -> str:
        return PY_VERSION

    @override
    def resource_dirs(self) -> HookReturn[Package]:
        from ._export import generated
        return generated

    @override
    def jinja_template_root(self) -> tuple[Package, str]:
        return hexdoc_hexical, "_templates"
