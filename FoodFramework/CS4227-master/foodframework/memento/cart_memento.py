from foodframework.memento.cart_state import CartState
from foodframework.memento.memento import Memento


class CartMemento(Memento):

    def __init__(self):
        """
        Create a CartMemento object.
        """
        self._state: CartState = None

    def get_state(self) -> CartState:
        """
        Get the state of the memento.
        :return: CartState
        """
        return self._state

    def set_state(self, cart_state: CartState) -> None:
        """
        Set the state of the memento.
        :param cart_state: The new state.
        """
        self._state = cart_state
