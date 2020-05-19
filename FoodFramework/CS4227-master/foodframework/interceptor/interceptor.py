from abc import ABC, abstractmethod
from foodframework.interceptor.context import Context


class Interceptor(ABC):

    @abstractmethod
    def event_callback(self, context: Context):
        pass
