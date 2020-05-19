from foodframework.command import Handler


class ErrorHandler(Handler):
    def execute(self, parameters: dict):
        """
        Client can extend this functionality by logging unsuccessful / critical
        events that occur throughout the application.
        :param parameters:
        """
        print(f"error handler {parameters}")
