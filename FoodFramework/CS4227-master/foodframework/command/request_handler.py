from foodframework.command import ErrorHandler, SuccessHandler, Handler


class RequestHandler:
    def __init__(self):
        self.handlers = dict()
        self.create_handlers()

    def create_handlers(self):
        """
        Client can extend this functionality by adding new handlers to
        monitor application behaviour throughout.
        """
        self.handlers["ERROR"] = ErrorHandler()
        self.handlers["SUCCESS"] = SuccessHandler()

    def lookup_handler(self, key: str):
        """

        :param key:
        :return:
        """
        return self.handlers[key]

    def handle(self, request_type, parameters: dict):
        """

        :param request_type:
        :param parameters:
        :return:
        """
        handler: Handler = self.lookup_handler(request_type)
        handler.execute(parameters)
