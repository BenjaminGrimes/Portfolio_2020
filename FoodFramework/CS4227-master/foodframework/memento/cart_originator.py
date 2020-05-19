from foodframework.memento.cart_memento import CartMemento
from foodframework.memento.cart_state import CartState
from foodframework.memento.originator import Originator


class CartOriginator(Originator):

    def __init__(self):
        """
        Create a CartOriginator object and instantiate the
        class data fields.
        """
        self._state: CartState = None

    def set_value(self, cart_state: CartState):
        """
        Set the value of the current state.
        :param cart_state: The new state
        """
        self._state = cart_state

    def get_value(self) -> CartState:
        """
        Return the value of the current state.
        :return: CartState
        """
        return self._state

    def create_memento(self) -> CartMemento:
        """
        Create a CartMemento object using the current state.
        :return: CartMemento
        """
        memento = CartMemento()
        memento.set_state(self._state)
        return memento

    def restore(self, memento: CartMemento):
        """
        Restore the state.
        :param memento: the state to use.
        """
        self._state = memento.get_state()
