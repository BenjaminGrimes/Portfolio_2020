from typing import List
from foodframework.factory_method.option import Option
from foodframework.template_method.food_item import FoodItem
from foodframework.bridge.allergen import Allergen

'''
Example of a Concrete Template class &
an example of a refined abstraction in Bridge D.P.
'''


class Burger(FoodItem):

    def __init__(self, allergen: Allergen, name: str, price: float, options: List[Option]):
        super().__init__(allergen, name, price, options)

    def get_name_details(self):
        return self.name

    def get_price_details(self):
        return self.price

    def get_options_details(self):
        details = str()
        for i in range(len(self.options)):
            details += str(i) + ", "
            if i == len(self.options):
                details += str(i)
        return details

    def get_type_details(self):
        return self.__class__.__name__.upper()

    # Bridge D.P. method
    def display_allergens(self):
        print(f"{self.name} contains ", end='')
        self.allergen.show_allergen()

    def display_item_details(self):
        print(f"Ordering burger: {self.name} for \u20ac{self.price}.")