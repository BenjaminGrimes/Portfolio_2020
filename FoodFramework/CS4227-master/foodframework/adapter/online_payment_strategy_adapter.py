from foodframework.adapter import OnlinePaymentStrategy
from foodframework.strategy import PaymentStrategy


class OnlinePaymentStrategyAdapter(PaymentStrategy):
    def __init__(self, online_payment_strategy: OnlinePaymentStrategy):
        self._online_payment_strategy = online_payment_strategy

    def pay(self, price: float = 0.0) -> None:
        self._online_payment_strategy.login("test@mail.ie", "password")
        self._online_payment_strategy.add_funds(price)
        self._online_payment_strategy.confirm_payment()
