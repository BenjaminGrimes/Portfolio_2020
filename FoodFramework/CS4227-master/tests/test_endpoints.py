from flask import Flask, url_for
from foodframework import db, app
import manage
import unittest
import pytest


class EndpointTest(unittest.TestCase):
    def setUp(self):
        self.status_ok: int = 200
        self.status_redirect: int = 302

    def test_login_ok(self):
        tester = app.test_client(self)
        response = tester.get("/login", content_type="html/text")

        self.assertEqual(response.status_code, self.status_ok)

    def test_register_ok(self):
        tester = app.test_client(self)
        response = tester.get("/register", content_type="html/text")

        self.assertEqual(response.status_code, self.status_ok)

    def test_home_ok(self):
        tester = app.test_client(self)
        response = tester.get("/home", content_type="html/text")

        self.assertEqual(response.status_code, self.status_ok)

    def test_menu_ok(self):
        tester = app.test_client(self)
        response = tester.get("/menu", content_type="html/text")

        self.assertEqual(response.status_code, self.status_ok)

    def test_logout_ok(self):
        tester = app.test_client(self)
        response = tester.get("/logout", content_type="html/text")

        self.assertEqual(response.status_code, self.status_redirect)

    def test_account_ok(self):
        tester = app.test_client(self)
        response = tester.get("/account", content_type="html/text")

        self.assertEqual(response.status_code, self.status_redirect)

    def test_order_ok(self):
        tester = app.test_client(self)
        response = tester.get("/order", content_type="html/text")

        self.assertEqual(response.status_code, self.status_redirect)

    def test_checkout_ok(self):
        tester = app.test_client(self)
        response = tester.get("/checkout", content_type="html/text")

        self.assertEqual(response.status_code, self.status_redirect)

    def test_confirm_ok(self):
        tester = app.test_client(self)
        response = tester.get("/confirm", content_type="html/text")

        self.assertEqual(response.status_code, self.status_redirect)

if __name__ == '__main__':
    unittest.main()
