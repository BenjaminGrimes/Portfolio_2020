from abc import ABC, abstractmethod
from foodframework.strategy.order import Order


class OrderItemBuilder(ABC):

    def __init__(self, email: str) -> None:
        self.order = Order(email)

    @abstractmethod
    def build_item(self, item) -> None:
        pass

    @abstractmethod
    def get_order(self) -> Order:
        pass
