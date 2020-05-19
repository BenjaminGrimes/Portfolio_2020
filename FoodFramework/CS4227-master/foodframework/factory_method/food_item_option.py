from foodframework.factory_method.option import Option


class FoodItemOption(Option):

    def __init__(self, name: str, price: float) -> None:
        """
        Create a FoodItemOption object.
        :param name: the name of the food item option.
        :param price: the price of the food item option.
        """
        super().__init__(name=name, price=price)

    def get_name(self) -> str:
        """
        Get the name of the food item option.
        :return: str
        """
        return self.name + " - \u20ac" + str(self.price)

    def get_price(self):
        """
        Get the price of the food item option.
        :return: float
        """
        return self.price

    def get_option_details(self):
        """
        Get details of the food item option.
        :return: str
        """
        return self.name + " - \u20ac" + str(self.price)
