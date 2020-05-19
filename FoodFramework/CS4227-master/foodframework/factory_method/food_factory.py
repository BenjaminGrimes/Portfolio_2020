from abc import ABC, abstractmethod


# Abstract Factory
class FoodFactory(ABC):

    @abstractmethod
    def create(self, name: str, price: float, type: str) -> None:
        pass
