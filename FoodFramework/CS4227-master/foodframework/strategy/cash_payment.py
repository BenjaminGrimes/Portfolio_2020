from foodframework.strategy import PaymentStrategy


class CashPayment(PaymentStrategy):
    def __init__(self, customer: str = None):
        self._customer_name = customer

    def pay(self, price: float = 0.0) -> None:
        print(f"{self._customer_name} is paying {price} with cash.")

    def __str__(self):
        return "cash"
