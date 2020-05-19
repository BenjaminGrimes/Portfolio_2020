import argparse
import re

from sqlalchemy.exc import IntegrityError

from foodframework import bcrypt, db
from foodframework.models import Customer, Role


def create_admin_role():
    if Role.query.filter_by(name="admin").first():
        return Role.query.filter_by(name="admin").first()

    return Role(name="admin", description="Administrator privileges")


def create_admin_user(role: Role, admin_email: str):
    hashed_password = bcrypt.generate_password_hash("admin").decode('utf-8')

    if Customer.query.filter_by(email=admin_email).first():
        print(f"{admin_email} already exists. Exiting.")
        exit(0)

    return Customer(email=admin_email, password=hashed_password, roles=[role])


def main(email):
    admin_role: Role = create_admin_role()
    admin: Customer = create_admin_user(admin_role, email)

    try:
        db.session.add(admin_role)
        db.session.add(admin)

        db.session.commit()

        print("Administrator created!")
    except IntegrityError as e:
        print(e)


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Admin creation script')
    parser.add_argument(
        "--e", type=str, help="supply admin email", required=True)
    args = parser.parse_args()

    if not re.match(r"^[A-Za-z0-9\.\+_-]+@[A-Za-z0-9\._-]+\.[a-zA-Z\.\+]", args.e):
        print("Invalid email")
        exit(0)

    main(args.e)
