from abc import ABC, abstractmethod


class Handler(ABC):
    @abstractmethod
    def execute(self, parameters: dict):
        pass
