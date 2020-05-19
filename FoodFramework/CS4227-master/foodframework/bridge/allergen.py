# Abstraction
from abc import ABC, abstractmethod


class Allergen(ABC):

    def __init__(self):
        self.allergen_desc = str()

    @abstractmethod
    def show_allergen(self):
        pass

    @abstractmethod
    def get_allergen_description(self):
        pass

    def __str__(self):
        return self.allergen_desc
