from typing import List
from foodframework.strategy import PaymentStrategy
from foodframework.template_method.food_item import FoodItem


class Order:
    def __init__(self, customer_email: str):
        self.customer_email = customer_email
        self.food_items: List[FoodItem] = list()
        self.payment_strategy: PaymentStrategy = None

    def add_item_to_order(self, food_item: FoodItem):
        self.food_items.append(food_item)

    def set_payment_strategy(self, payment_strategy: PaymentStrategy):
        self.payment_strategy = payment_strategy

    def order_food_items(self):
        order_price: float = 0
        for item in self.food_items:
            item.display_item_details()
            order_price = order_price + item.price
        self.payment_strategy.pay(order_price)

    def get_food_items_as_dict(self):
        order_items: dict = dict()

        for index, item in enumerate(self.food_items):
            order_items[index] = str(item)
        return order_items

    def get_order_price(self):
        order_price: float = 0.0

        for item in self.food_items:
            order_price += item.sum_price()

        return round(order_price, 2)

    def __str__(self):
        details = "ORDER DETAILS:\n"
        details += "\tEMAIL: " + self.customer_email + "\n"
        details += "\tPAYMENT: " + str(self.payment_strategy) + "\n"
        details += "\tORDER:\n"
        for item in self.food_items:
            details += "\t" + str(item)
        return details
