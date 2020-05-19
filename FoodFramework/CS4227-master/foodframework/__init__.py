from flask import Flask
from flask_admin import Admin
from flask_sqlalchemy import SQLAlchemy
import stripe
import os
from flask_bcrypt import Bcrypt
from flask_login import LoginManager
from flask_migrate import Migrate

app = Flask(__name__)
app.config['SECRET_KEY'] = 'a4758a2eb799df6430723f05aa19e169'
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///site.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['BASE_DIR'] = app.root_path

db = SQLAlchemy(app)
migrate = Migrate(app, db)
bcrypt = Bcrypt(app)

login_manager = LoginManager(app)
login_manager.login_view = 'login'  # function name of the route
login_manager.login_message_category = 'info'  # set to bootstrap info class

from foodframework.models import *
from foodframework.custom_views import MenuFileView, AdminView, MyAdminIndexView

admin = Admin(app, name='Food Framework',
              index_view=MyAdminIndexView(url='/admin'),
              endpoint='admin')

admin.add_view(AdminView(Customer, db.session))
admin.add_view(AdminView(Role, db.session))
admin.add_view(AdminView(FoodItem, db.session))
admin.add_view(AdminView(Option, db.session))
admin.add_view(AdminView(MenuFile, db.session))
admin.add_view(MenuFileView(name='Upload menu file', endpoint='upload'))

# In the terminal input the following
# set STRIPE_PUBLISHABLE_KEY=pk_test_D4b6bM4IzSqHWMOpUembiuRa00DHhcERxc
# set STRIPE_SECRET_KEY=sk_test_lhXL2dbsAaL6CNGvFhKnmZwz00V1gn9Rms
stripe_keys = {
    'secret_key': os.getenv('STRIPE_SECRET_KEY', 'sk_test_lhXL2dbsAaL6CNGvFhKnmZwz00V1gn9Rms'),
    'publishable_key': os.getenv('STRIPE_PUBLISHABLE_KEY', 'pk_test_D4b6bM4IzSqHWMOpUembiuRa00DHhcERxc')
}
stripe.api_key = stripe_keys['secret_key']

# Create the concrete interceptor
from foodframework.interceptor.concrete_interceptor import ConcreteInterceptor
concrete_interceptor = ConcreteInterceptor()

# Create the originator for the Memento
from foodframework.memento.cart_originator import CartOriginator
from foodframework.memento.cart_caretaker import CartCaretaker
cart_originator = CartOriginator()
cart_caretaker = CartCaretaker()
cart_caretaker.set_originator(cart_originator)


# Create the dispatcher
from foodframework.interceptor.logging_dispatcher import LoggingDispatcher
logging_dispatcher = LoggingDispatcher()
logging_dispatcher.register(concrete_interceptor)

from foodframework import routes
