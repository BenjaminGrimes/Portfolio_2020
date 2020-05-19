import os

import jsonpickle
import stripe
from flask import (flash, redirect, render_template, request,
                   send_from_directory, session, url_for)
from flask_login import current_user, login_required, login_user, logout_user

from foodframework import app, bcrypt, cart_caretaker, db, logging_dispatcher
from foodframework.builder import (ConcreteItemBuilder, OrderItemBuilder,
                                   OrderItemDirector)
from foodframework.command import RequestHandler
from foodframework.email import OrderEmailSender, IEmailSender
from foodframework.forms import (CheckoutCollectionForm, CheckoutDeliveryForm,
                                 LoginForm, RegistrationForm)
from foodframework.interceptor.context import Context
from foodframework.memento.cart_state import CartState
from foodframework.models import Customer, MenuFile
from foodframework.strategy import Order, SimplePaymentFactory


@app.route("/")
@app.route("/home")
def home():
    """
    Set up home page
    """

    '''
    about.txt holds information which is extracted and displayed on the home page
    Allows Client to enter a custom file, unique to their business
    '''
    document_path = os.path.join(app.config['BASE_DIR'], 'about.txt')
    text = open(document_path, 'r+')
    content = text.read()
    text.close()
    info = content.split(";")
    title = info[0]
    about_text = info[1]
    contact_email = info[2]
    address = info[3]

    return render_template('home.html', title=title, about_text=about_text, contact_email=contact_email,
                           address=address)


def handle_logging_dispatch(parameters: dict, status: str):
    request_handler: RequestHandler = RequestHandler()
    request_handler.handle(status, parameters)


@app.route("/register", methods=['GET', 'POST'])
def register():
    """
    Register a new Customer
    """
    if current_user.is_authenticated:
        return redirect(url_for('home'))
    form = RegistrationForm()
    # Pass valid register params
    if form.validate_on_submit():
        from foodframework.models import Customer
        hashed_password = bcrypt.generate_password_hash(
            form.password.data).decode('utf-8')
        added = add_new_customer(
            email=form.email.data, password=hashed_password)
        if added:
            db.session.commit()
            flash(f'Account created for {form.email.data}!', 'success')
            return redirect(url_for('login'))
        else:
            parameters = {"message": "An error occured during registration."}
            handle_logging_dispatch(parameters, "ERROR")
    return render_template('register.html', title='Register', form=form)


def add_new_customer(email, password):
    # noinspection PyBroadException
    try:
        customer_exists = Customer.query.filter_by(email=email).first()
        if customer_exists is None:
            new_customer = Customer(email=email, password=password)
            db.session.add(new_customer)
            return True
        else:
            return False
    except Exception:
        return False


@app.route("/login", methods=['GET', 'POST'])
def login():
    """
    Login a registered Customer
    """
    if current_user.is_authenticated:
        return redirect(url_for('home'))

    form = LoginForm()
    # Pass valid login params
    if form.validate_on_submit():
        from foodframework.models import Customer
        customer = Customer.query.filter_by(email=form.email.data).first()

        # If customer details match existing customer in database continue
        if customer and bcrypt.check_password_hash(customer.password, form.password.data):
            login_user(customer, remember=form.remember.data)
            next_page = request.args.get('next')
            context = Context()
            context.set_value(
                "Customer: " + form.email.data + " has logged in.")
            logging_dispatcher.callback(context)

            # Redirect logged in customer to home page
            return redirect(next_page) if next_page else redirect(url_for('home'))
        else:
            flash('Login Unsuccessful. Please check your email and password.', 'danger')
            parameters = {"message": "An error occured during login."}
            handle_logging_dispatch(parameters, "ERROR")

    return render_template('login.html', title='Login', form=form)


@app.route("/logout")
@login_required
def logout():
    """
    Logout logged in Customer
    """
    context = Context()
    context.set_value("Customer: " + current_user.email + " has logged out.")
    logging_dispatcher.callback(context)
    logout_user()
    return redirect(url_for('login'))


@app.route('/account')
@login_required
def account():
    return render_template('account.html', title='Account')


@app.route('/menu')
def menu():
    """
    MenuFile is a pdf file which the Client can upload
    Allows the Client to upload their menu in pdf format
    """
    menu = MenuFile.query.first()
    path, filename = menu.path_to_file.rsplit('\\', 1)
    path = os.path.join(app.root_path, path)
    return send_from_directory(directory=path, filename=filename)


@app.route('/order', methods=['GET', 'POST'])
@login_required
def order():
    """
    Order method: Allows Customer to choose multiple foodItems with whatever options they wish to have
    """
    if request.method == 'GET':
        # Get all foodItems in database
        food_items = get_food_items()
        cart_state = cart_caretaker.get_value()
        cart_items = []
        if cart_state is not None:
            print(cart_state.cart)
            cart_items = jsonpickle.loads(cart_state.cart)["cart_items"]
            for i in range(len(cart_items)):
                cart_items[i] = jsonpickle.loads(cart_items[i])
        print(cart_items)
        return render_template('order.html', items=food_items, title='Place an Order', cart=cart_items)
    cart = request.json['items']
    # Builder Design Pattern builds an order made up of different foodItems and Options
    builder: OrderItemBuilder = ConcreteItemBuilder(email=current_user.email)
    director: OrderItemDirector = OrderItemDirector(order_item_builder=builder)

    cart = jsonpickle.loads(cart[0])
    new_order = director.construct_order(cart["cart_items"])
    session["order"] = jsonpickle.dumps(new_order)

    return ""  # ,204 <-- add this to remove warning?
    # Ajax handles the redirect - otherwise 2 GET requests are sumbitted to /checkout


@app.route('/save_cart_state', methods=['POST'])
def save_cart_state():
    cart = request.get_json()
    state = CartState(jsonpickle.dumps(cart))
    cart_caretaker.set_value(state)
    return '', 204


@app.route('/cart_undo', methods=['GET', 'POST'])
def cart_undo():
    cart_caretaker.undo_operation()
    return redirect(url_for('order'))


def get_food_items():
    from foodframework.models import FoodItem
    all_food_items = FoodItem.query.all()
    food_items = []

    for food_item in all_food_items:
        # Get all options of this food item.
        item = {
            'name': food_item.name,
            'description': food_item.description,
            'price': food_item.price,
            'type': food_item.type,
            'options': []
        }
        # Link all relevant options to their parent foodItems
        for option in food_item.options.all():
            item['options'].append(option)
        food_items.append(item)
    return food_items


@app.route("/checkout", methods=['GET', 'POST'])
@login_required
def checkout():
    """
    Checkout, User presses checkout button
    Items from Order have been confirmed and user is prompted to choose a payment option.
    The Strategy Design Pattern allows the framework to use whatever payment method the Customer chooses at runtime
    """
    if request.method == "GET":
        checkout_order = jsonpickle.loads(session.get("order"))
        food_items = checkout_order.food_items
        return render_template('checkout.html', items=food_items)

    payment: str = request.form["payment"]
    collection_delivery: str = request.form["type"]
    payment_strategy: PaymentStrategy = SimplePaymentFactory.create_payment_strategy(
        payment, current_user.email)

    # NOTE - Adapting a modern online payment to our payment strategy interface
    # paypal_payment: OnlinePaymentStrategyAdapter = OnlinePaymentStrategyAdapter(PaypalPayment())
    # paypal_payment.pay(100)

    parameters = {"logged_user": current_user.email,
                  "message": "Checkout has been confirmed."}
    handle_logging_dispatch(parameters, "SUCCESS")

    return redirect(url_for('confirm', payment=str(payment_strategy), collection_delivery=collection_delivery))


@app.route("/confirm", methods=['GET', 'POST'])
@login_required
def confirm():
    payment_strategy: str = request.args.get('payment')
    collection_delivery: str = request.args.get('collection_delivery')
    form = CheckoutCollectionForm(
    ) if collection_delivery == "collection" else CheckoutDeliveryForm()
    checkout_order: Order = jsonpickle.loads(session.get("order"))
    order_price: float = checkout_order.get_order_price()

    from foodframework import stripe_keys

    if form.validate_on_submit():
        send_order_confirmation_email()
        flash("Order has been confirmed!", 'success')
        return redirect(url_for('home'))

    return render_template('confirm_order.html', payment=payment_strategy, key=stripe_keys['publishable_key'],
                           form=form, confirmation_type=collection_delivery.upper(),
                           title="Order Confirmation", amount=int(order_price * 100))


def send_order_confirmation_email():
    checkout_order: Order = jsonpickle.loads(session.get("order"))
    items_dict: dict = checkout_order.get_food_items_as_dict()

    email_confirmation_sender: IEmailSender = OrderEmailSender()
    email_confirmation_sender.set_email_content(content=items_dict)
    email_confirmation_sender.send_email(
        recipient=current_user.email)


@app.route("/card-payment", methods=['POST'])
def card_payment():
    """
    Leveraging the Stripe Payment System to allow customers to use secure online
    payments to complete the purchase of their order
    """
    from stripe.error import StripeError
    try:
        amount = request.form['amount']
        email = request.form['stripeEmail']
        customer = stripe.Customer.create(
            email=email, source=request.form['stripeToken'])
        stripe.Charge.create(
            customer=customer.id,
            amount=amount,
            currency='eur',
            description='Flask Charge'
        )
        send_order_confirmation_email()
        flash("Order has been confirmed!", 'success')
        return redirect(url_for('home'))
    except StripeError as se:
        return str(se)
