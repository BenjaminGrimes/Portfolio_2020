from foodframework.interceptor.context import Context
from foodframework.interceptor.concrete_interceptor import ConcreteInterceptor


class LoggingDispatcher:

    def __init__(self):
        """
        Create the LoggingDispatcher object and initialize a list of interceptors.
        """
        self.interceptors = list()

    def callback(self, context: Context):
        """
        Invoke the event_callback function on all interceptors.
        :param context: The context object to use.
        """
        for interceptor in self.interceptors:
            interceptor.event_callback(context)

    def register(self, concrete_interceptor: ConcreteInterceptor):
        """
        Register a new interceptor by adding it to the list of interceptors.
        :param concrete_interceptor: The new interceptor to register.
        """
        self.interceptors.append(concrete_interceptor)

    def unregister(self, concrete_interceptor: ConcreteInterceptor):
        """
        Unregister an interceptor by removing it from the list of interceptors.
        :param concrete_interceptor: The interceptor to remove.
        """
        self.interceptors.remove(concrete_interceptor)
