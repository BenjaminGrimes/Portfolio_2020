from foodframework.interceptor.logger import Logger


class Context:

    def __init__(self):
        """
        Instantiate data fields of value and logger.
        """
        self.value = str()
        self.logger = Logger()

    def get_value(self):
        """
        Get the value of the context.
        :return: str
        """
        return self.value

    def set_value(self, value: str):
        """
        Set the value of the context.
        :param value: the new value of the context
        """
        self.value = value

    def consume_service(self):
        """
        Consume the service. Invoke the info function on the logger
        passing the value of the context as the message to display.
        :return:
        """
        self.logger.info(self.value)
