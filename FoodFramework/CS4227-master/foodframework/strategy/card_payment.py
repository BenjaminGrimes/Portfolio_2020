from foodframework.strategy import PaymentStrategy


class CardPayment(PaymentStrategy):
    def __init__(self, card_type: str = 'debit', card_holder_name: str = None, expiry: str = None):
        self._card_type = card_type  # _ constitutes private
        self._card_holder_name = card_holder_name
        self._expiry = expiry

    def set_card_details(self, card_type: str, expiry: str):
        self._card_type = card_type
        self._expiry = expiry

    def pay(self, price: float = 0.0) -> None:
        # May do some card payment algorithmic checks here - Strategy
        print(f"{self._card_holder_name} is paying {price} with {self._card_type} card")

    def __str__(self):
        return "card"
