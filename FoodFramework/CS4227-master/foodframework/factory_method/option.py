from abc import ABC, abstractmethod


class Option(ABC):

    def __init__(self, name: str, price: float):
        """
        :param name: the name of the option.
        :param price: the price of the option.
        """
        self.name = name
        self.price = price

    def __str__(self):
        return f"Name: {self.name}, Price: {self.price}."

    @abstractmethod
    def get_name(self):
        pass

    @abstractmethod
    def get_price(self):
        return self.price

    @abstractmethod
    def get_option_details(self):
        pass
