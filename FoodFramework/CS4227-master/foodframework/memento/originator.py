from abc import ABC, abstractmethod
from foodframework.memento.memento import Memento


class Originator(ABC):

    @abstractmethod
    def restore(self, memento: Memento):
        pass

    @abstractmethod
    def create_memento(self):
        pass
