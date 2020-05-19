from abc import ABC, abstractmethod
from typing import List
from foodframework.factory_method.option import Option

'''
Template Design Method and Bridge Design Pattern used here
'''


# FoodItem interface which all FoodItems inherit from
class FoodItem(ABC):

    def __init__(self, allergen, name: str, price: float, options: List[Option]):
        self.name = name
        self.price = price
        self.options: List[Option] = options
        self.allergen = allergen

    def sum_price(self):
        full_price = self.price
        for i in range(len(self.options)):
            full_price += self.options[i].get_price()

        return full_price

    def get_details(self):
        item = dict()
        item["name"] = self.get_name_details()
        item["price"] = self.get_price_details()
        item["options"] = self.get_options_details()
        item["type"] = self.get_type_details()
        return item

    @abstractmethod
    def get_name_details(self):
        pass

    @abstractmethod
    def get_price_details(self):
        pass

    @abstractmethod
    def get_options_details(self):
        pass

    @abstractmethod
    def get_type_details(self):
        pass

    # Bridge D.P. functions
    @abstractmethod
    def display_allergens(self):
        pass

    @abstractmethod
    def display_item_details(self):
        pass

    def __str__(self):
        full_price = "%.2f" % round(self.sum_price(), 2)
        details = self.name + " - \u20ac" + str(self.price) + "\n"
        for option in self.options:
            details += "\t->" + str(option) + "\n"
        details += "\tTotal price: \u20ac" + full_price + "\n"
        details += "\tAllergen: " + str(self.allergen.allergen_desc) + "\n"
        return details
