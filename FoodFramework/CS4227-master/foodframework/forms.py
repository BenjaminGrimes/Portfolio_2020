from flask_wtf import FlaskForm
from wtforms import StringField, PasswordField, SubmitField, BooleanField, ValidationError
from flask_wtf.file import FileField, FileRequired
from wtforms.validators import DataRequired, Email, EqualTo
from foodframework.models import Customer


class RegistrationForm(FlaskForm):
    email = StringField('Email',
                        validators=[DataRequired(), Email()])
    password = PasswordField('Password', validators=[DataRequired()])
    confirm_password = PasswordField('Confirm Password',
                                     validators=[DataRequired(), EqualTo('password')])
    submit = SubmitField('Register')

    def validate_email(self, email):
        customer = Customer.query.filter_by(email=email.data).first()
        if customer:
            raise ValidationError(
                'That email is already in use, please choose a different one.')


class LoginForm(FlaskForm):
    email = StringField('Email',
                        validators=[DataRequired(), Email()])
    password = PasswordField('Password', validators=[DataRequired()])
    remember = BooleanField('Remember Me')
    submit = SubmitField('Login')


class UploadForm(FlaskForm):
    file_name = StringField('File name', validators=[DataRequired()])
    file = FileField(validators=[FileRequired()])
    submit = SubmitField('Upload')


class CheckoutCollectionForm(FlaskForm):
    first_name = StringField('Fist Name', validators=[DataRequired()])
    last_name = StringField('Last Name', validators=[DataRequired()])
    phone_number = StringField('Phone Number', validators=[DataRequired()])
    submit = SubmitField("Confirm Collection")

    def validate_phone_number(self, phone_number):
        if len(phone_number.data) > 16 or len(phone_number.data) < 5:
            raise ValidationError('Please provide a valid phone number.')
        import phonenumbers
        try:
            number = phonenumbers.parse(phone_number, "IE")
            if not (phonenumbers.is_valid_number(number)):
                raise ValidationError('Please provide a valid phone number.')
        except:
            number = phonenumbers.parse("+353" + phone_number.data)
            if not (phonenumbers.is_valid_number(number)):
                raise ValidationError('Please provide a valid phone number.')


class CheckoutDeliveryForm(FlaskForm):
    first_name = StringField('First Name', validators=[DataRequired()])
    last_name = StringField('Last Name', validators=[DataRequired()])
    phone_number = StringField('Phone Number', validators=[DataRequired()])

    address_line_1 = StringField('Address', validators=[DataRequired()])
    address_line_2 = StringField(validators=[DataRequired()])
    address_line_3 = StringField(validators=[DataRequired()])
    address = StringField('Street Address')
    submit = SubmitField("Confirm Delivery")

    def validate_phone_number(self, phone_number):
        if len(phone_number.data) > 16 or len(phone_number.data) < 5:
            raise ValidationError('Please provide a valid phone number.')
        import phonenumbers
        try:
            number = phonenumbers.parse(phone_number, "IE")
            if not (phonenumbers.is_valid_number(number)):
                raise ValidationError('Please provide a valid phone number.')
        except:
            number = phonenumbers.parse("+353" + phone_number.data)
            if not (phonenumbers.is_valid_number(number)):
                raise ValidationError('Please provide a valid phone number.')
