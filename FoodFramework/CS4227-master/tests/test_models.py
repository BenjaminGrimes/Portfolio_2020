from foodframework import db, app
from foodframework.routes import add_new_customer, cart_undo
from foodframework.models import Customer, Role, FoodItem, Option

import unittest

# python -m pytest tests/


class ModelsTest(unittest.TestCase):
    def test_new_customer_added_successfully(self):
        test_password: str = "password"
        test_email: str = "test_user@mail.ie"

        result = add_new_customer(test_email, test_password)
        assert result

    def test_customer_already_exists(self):
        test_password: str = 'password'
        test_email: str = 'admin@admin.ie'
        exists = add_new_customer(test_email, test_password)
        self.assertEqual(exists, False)

    def test_new_customer(self):
        customer = Customer(email='test@mail.ie', password='password')

        self.assertEqual(customer.email, "test@mail.ie")
        self.assertEqual(customer.password, "password")
        self.assertEqual(len(customer.roles), 0)

    def test_new_role(self):
        admin_role = Role(name="admin", description="Administrator Role")

        self.assertEqual(admin_role.name, "admin")
        self.assertEqual(admin_role.description, "Administrator Role")

    def test_admin_role(self):
        admin_role = Role(name="admin", description="Administrator Role")
        admin = Customer(email='admin@mail.ie',
                         password='password', roles=[admin_role])

        self.assertEqual(len(admin.roles), 1)
        self.assertEqual(admin.roles[0].name, "admin")

    def test_customer_role(self):
        customer_role = Role(name="customer", description="End user role")
        admin = Customer(email='test@mail.ie',
                         password='password', roles=[customer_role])

        self.assertEqual(len(admin.roles), 1)
        self.assertEqual(admin.roles[0].name, "customer")

    def test_new_food_item(self):
        foodItem = FoodItem(name='testPanini', description='Hot Panini', price='10.00', type='PANINI')

        self.assertEqual(foodItem.name, "testPanini")
        self.assertEqual(foodItem.description, "Hot Panini")
        self.assertEqual(foodItem.price, "10.00")
        self.assertEqual(foodItem.type, "PANINI")

    def test_new_option(self):
        option = Option(name='testPaniniOption', price='4.5', type='PANINI')

        self.assertEqual(option.name, "testPaniniOption")
        self.assertEqual(option.price, "4.5")
        self.assertEqual(option.type, "PANINI")

    def test_food_item_with_option(self):
        option = Option(name='testPaniniOption', price='4.5', type='PANINI')
        foodItem = FoodItem(name='testPanini', description='Hot Panini', price='10.00', type='PANINI', options=[option])

        self.assertEqual(foodItem.options[0].name, "testPaniniOption")

