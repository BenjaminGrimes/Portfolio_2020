# Refined Abstraction
from foodframework.bridge.allergen import Allergen


class Milk(Allergen):

    def __init__(self):
        super().__init__()
        self.allergen_desc = 'Milk'

    def get_allergen_description(self) -> str:
        return self.allergen_desc

    def show_allergen(self):
        print(self.allergen_desc)
