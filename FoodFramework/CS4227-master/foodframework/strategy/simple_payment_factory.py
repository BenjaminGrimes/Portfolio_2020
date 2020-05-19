from foodframework.strategy import PaymentStrategy, CardPayment, CashPayment
from foodframework.adapter import OnlinePaymentStrategyAdapter, PaypalPayment


class SimplePaymentFactory:
    @staticmethod
    def create_payment_strategy(payment_type: str, customer: str) -> PaymentStrategy:
        if payment_type == "card":
            return CardPayment(card_holder_name=customer)
        elif payment_type == "cash":
            return CashPayment(customer=customer)
        elif payment_type == "paypal":
            return OnlinePaymentStrategyAdapter(PaypalPayment())
