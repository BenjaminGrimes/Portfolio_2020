import os
import unittest

from foodframework import app, db

TEST_DB = 'test.db'
# https://www.patricksoftwareblog.com/unit-testing-a-flask-application/


class BasicTests(unittest.TestCase):

    # Setup and teardown

    # execute before each test
    def setUp(self):
        app.config['TESTING'] = True
        app.config['WTF_CSRF_ENABLED'] = False
        app.config['DEBUG'] = False
        app.config['SQL_ALCHEMY_DATABASE_URI'] = 'sqlite:///' + \
            os.path.join(app.config['BASE_DIR'], TEST_DB)
        self.app = app.test_client()

    def tearDown(self):
        pass

    # helper methods
    def register(self, email: str, password: str):
        pass

    def login(self, email: str, password: str):
        pass

    def logout(self):
        pass

    def test_valid_customer_registration(self):
        pass

    def test_invalid_customer_registration(self):
        pass


if __name__ == "__main__":
    unittest.main()
