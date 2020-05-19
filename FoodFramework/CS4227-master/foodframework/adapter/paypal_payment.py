from foodframework.adapter import OnlinePaymentStrategy


class PaypalPayment(OnlinePaymentStrategy):
    def login(self, username: str, password: str):
        print(f"Paypal login: {username}")

    def add_funds(self, amount: float):
        print(f"Paypal adding funds to account: {amount}")

    def confirm_payment(self):
        print(f"Paypal confirming payment..")
