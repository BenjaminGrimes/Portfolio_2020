from typing import List
from foodframework.factory_method import Option, OptionFactory
from foodframework.builder.order_item_builder import OrderItemBuilder
from foodframework.simple_factory.food_factory import SimpleFoodItemFactory
from foodframework.strategy.order import Order


class ConcreteItemBuilder(OrderItemBuilder):

    def __init__(self, email: str) -> None:
        """
        Create a ConcreteItemBuilder object
        :param email: a str representing the email address of the customer ordering.
        """
        super().__init__(email)

    def build_item(self, item) -> None:
        """
        Build a food item from a dict.
        :param item: dict
        :return: None
        """
        options: List[Option] = []
        factory: OptionFactory = OptionFactory()

        for option in item["options"]:
            new_option = factory.create(
                name=option["name"], price=option["price"], option_type="OPTION")
            options.append(new_option)

        # Need to parameterize everything into a list for simple_factory
        factory_dict = {
            "name": item["name"],
            "price": item["price"],
            "type": item["type"],
            "options": options
        }
        '''
        Using a SimpleFoodItemFactory to create the FoodItems the customer picks
        '''
        food_item = SimpleFoodItemFactory.create_food_item(factory_dict)
        food_item.display_allergens()
        self.order.add_item_to_order(food_item)

    def get_order(self) -> Order:
        """
        Get the Order object built by the builder
        :return: Order
        """
        return self.order
