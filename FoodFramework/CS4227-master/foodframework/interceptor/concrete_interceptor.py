from foodframework.interceptor.interceptor import Interceptor
from foodframework.interceptor.context import Context


class ConcreteInterceptor(Interceptor):

    def event_callback(self, context: Context):
        """
        Invoke consume_service on the context object.
        :param context: the context object
        """
        context.consume_service()
