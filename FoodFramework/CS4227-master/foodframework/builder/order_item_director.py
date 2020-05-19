from foodframework.builder.order_item_builder import OrderItemBuilder
import jsonpickle
from foodframework.strategy.order import Order


class OrderItemDirector:

    def __init__(self, order_item_builder: OrderItemBuilder) -> None:
        """
        Create a OrderItemDirector object.
        :param order_item_builder: the builder object to use.
        """
        self.builder = order_item_builder

    def construct_order(self, cart) -> Order:
        """
        Construct/build the order using the list of items in the cart.
        :param cart: A list of items in JSON format
        :return: Order
        """
        # Construct the FoodItems using the builder interface
        for item in cart:
            item = jsonpickle.loads(item)
            self.builder.build_item(item=item)
        return self.builder.get_order()
