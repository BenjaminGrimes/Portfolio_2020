from foodframework.email import IEmailSender


class OrderEmailSender(IEmailSender):
    def __init__(self):
        super().__init__(subject='Your Order Confirmation')

    def set_email_content(self, content: dict):
        self._email_content = content

    def send_email(self, recipient: str):
        body: str = f"""
        Hey there,
        
        Your order details are as follows:
        {self._email_content}

        Thank you very much for your custom,
        The Bagel Boys.
        """

        super().send_email(recipient=recipient, body=body)
