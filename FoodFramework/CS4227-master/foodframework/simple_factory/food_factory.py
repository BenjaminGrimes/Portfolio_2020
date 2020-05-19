from foodframework.bridge import Milk, Gluten
from foodframework.template_method import Bagel, Burger, Pizza


class SimpleFoodItemFactory:

    @staticmethod
    def create_food_item(details: dict):
        """
        A creation function that creates a food item.
        :param details:
        :return: FoodItem
        """
        created_food_item = SimpleFoodItemFactory.make_food_item(details)
        return created_food_item

    @staticmethod
    def make_food_item(details: dict):
        """
        A creation method that uses the type of an item to create a FoodItem object.
        :param details: the details of the item to create.
        :return: FoodItem
        """
        item_type: str = details["type"].upper()

        if item_type == "BAGEL":
            return Bagel(allergen=Milk(), name=details["name"], price=details["price"], options=details["options"])
        elif item_type == "BURGER":
            return Burger(allergen=Gluten(), name=details["name"], price=details["price"], options=details["options"])
        elif item_type == "PIZZA":
            return Pizza(allergen=Gluten(), name=details["name"], price=details["price"], options=details["options"])