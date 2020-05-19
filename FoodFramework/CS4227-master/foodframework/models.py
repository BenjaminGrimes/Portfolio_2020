from datetime import datetime
from foodframework import db, login_manager
from flask_login import UserMixin
from flask_security import RoleMixin

'''
This models file contains all of the DB Tables used by SQLALCHEMY
This allows the framework to use ORM
'''


@login_manager.user_loader
def load_customer(customer_id):
    return Customer.query.get(int(customer_id))


roles_users = db.Table(
    'roles_users',
    db.Column('customer_id', db.Integer(), db.ForeignKey('customer.id')),
    db.Column('role_id', db.Integer(), db.ForeignKey('role.id'))
)


# Customer Table
class Customer(db.Model, UserMixin):
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    email = db.Column(db.String(120), unique=True, nullable=False)
    password = db.Column(db.String(60), nullable=False)
    last_login = db.Column(db.DateTime, nullable=False,
                           default=datetime.utcnow)
    roles = db.relationship(
        'Role',
        secondary=roles_users,
        backref=db.backref('users', lazy='dynamic')
    )

    def __repr__(self):
        return f"Customer('{self.email}')"


# Customer Role Table
class Role(db.Model, RoleMixin):
    id = db.Column(db.Integer(), primary_key=True, autoincrement=True)
    name = db.Column(db.String(80), unique=True, nullable=False)
    description = db.Column(db.String(255))

    def __str__(self):
        return self.name

    def __hash__(self):
        return hash(self.name)


# FoodItem Table
class FoodItem(db.Model):
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    name = db.Column(db.String(64), unique=False, nullable=False)
    description = db.Column(db.String(120), unique=False, nullable=False)
    price = db.Column(db.Float(precision=2), unique=False, nullable=False)
    type = db.Column(db.String(64), nullable=True, default='MAIN')
    options = db.relationship('Option', backref="option_type", lazy='dynamic')

    def __repr__(self):
        return f"FoodItem('{self.name}', '{self.description}', '{self.price}', '{self.type}')"


# Option Table
class Option(db.Model):
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    name = db.Column(db.String(64), nullable=False)
    price = db.Column(db.Float(precision=2), unique=False, nullable=False)
    type = db.Column(db.String(64), db.ForeignKey('food_item.type'), nullable=False, default='MAIN')

    def __repr__(self):
        return f"Option('{self.name}', '{self.price}', '{self.type}')"


# MenuFile Table
class MenuFile(db.Model):
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    name = db.Column(db.String(60), nullable=False)
    # data = db.Column(db.LargeBinary, nullable=False)
    path_to_file = db.Column(db.String(60), nullable=False)

    def __repr__(self):
        return f"MenuFile('{self.name}')"
