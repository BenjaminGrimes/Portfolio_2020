from foodframework.command import Handler


class SuccessHandler(Handler):
    def execute(self, parameters):
        """
        Client can extend this functionality by logging successful events that
        occur throughout the application.
        :param parameters:
        """
        print(f"Success: {parameters}")
