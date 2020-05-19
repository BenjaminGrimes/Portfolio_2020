from foodframework.memento.cart_originator import CartOriginator
from foodframework.memento.cart_state import CartState


class CartCaretaker:

    def __init__(self):
        """
        Create the Caretaker Object.
        This is responsible for keep the memento objects safe.
        Instantiate a list of mementos and originators to hold previous state.
        Instantiate an CartOriginator object to hold the current state.
        """
        self._originator: CartOriginator = None
        self._mementos = list()
        self._originators = list()

    def set_originator(self, originator: CartOriginator):
        """
        Set the current originator.
        :param originator: The new originator object.
        """
        self._originator = originator

    def undo_operation(self):
        """
        Perform an undo operation by popping the top originator and memento from
        the mementos and originators lists.
        """
        if len(self._originators) > 0 and len(self._mementos) > 0:
            cart_originator: CartOriginator = self._originators.pop()
            cart_originator.restore(self._mementos.pop())

    def set_value(self, value: CartState):
        """
        Set the value of the originator by adding the new memento to the mementos list,
        adding the current originator to the originator list and setting the originator to
        its new state.
        :param value: The cart state to set.
        """
        # Append the current state of the cart to list
        self._mementos.append(self._originator.create_memento())
        self._originators.append(self._originator)
        # Set the new state
        self._originator.set_value(value)

    def get_value(self):
        """
        Get the value of the originator.
        :return: CartState
        """
        return self._originator.get_value()
