from abc import ABC, abstractmethod
import smtplib


class IEmailSender(ABC):
    def __init__(self, subject: str):
        self._port = 465
        self._subject = subject
        self._host = 'smtp.gmail.com'
        self._sender_email = 'cs4227.project@gmail.com'
        self._sender_password = 'ilovepatterns'  # TODO env variable for secrecy?
        self._init_smtplib()

    def _init_smtplib(self):
        try:
            self._email_server = smtplib.SMTP_SSL(
                host=self._host, port=self._port)
            self._email_server.ehlo()
            self._email_server.login(self._sender_email, self._sender_password)
        except smtplib.SMTPException as e:
            print(e)

    @abstractmethod
    def set_email_content(self, content: dict):
        pass

    @abstractmethod
    def send_email(self, recipient: str, body: str):
        try:
            email_text = \
                f'From: {self._sender_email}\n' \
                f'To: {recipient}\n' \
                f'Subject: {self._subject}\n\n' \
                f'{body}'.encode()

            self._email_server.sendmail(
                self._sender_email, recipient, email_text)
            self._email_server.close()
            print("Email sent.")
        except smtplib.SMTPException as e:
            print(e)
