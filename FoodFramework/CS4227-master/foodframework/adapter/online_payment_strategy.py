from abc import ABC, abstractmethod


class OnlinePaymentStrategy(ABC):
    @abstractmethod
    def login(self, username: str, password: str):
        pass

    @abstractmethod
    def add_funds(self, amount: float):
        pass

    @abstractmethod
    def confirm_payment(self):
        pass
