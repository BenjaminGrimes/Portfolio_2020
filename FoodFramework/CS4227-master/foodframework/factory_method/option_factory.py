from foodframework.factory_method import FoodFactory
from foodframework.factory_method.option import Option
from foodframework.factory_method.food_item_option import FoodItemOption


# SimpleOptionFactory
class OptionFactory(FoodFactory):

    def create(self, name: str, price: float, option_type: str) -> Option:
        """
        A creation function that creates a FoodItemOption
        :param name: The name of the option.
        :param price: the price of the option.
        :param option_type: the type of the option.
        :return: FoodItemOption
        """
        return FoodItemOption(name=name, price=price)
