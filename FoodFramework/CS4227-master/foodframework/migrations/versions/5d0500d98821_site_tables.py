"""site_tables

Revision ID: 5d0500d98821
Revises: 75bdea7372e2
Create Date: 2019-11-03 20:02:00.225130

"""
from alembic import op
import sqlalchemy as sa

# revision identifiers, used by Alembic.
revision = '5d0500d98821'
down_revision = '75bdea7372e2'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    with op.batch_alter_table('food_item', schema=None) as batch_op:
        batch_op.alter_column('type',
                              existing_type=sa.VARCHAR(length=64),
                              nullable=False)

    with op.batch_alter_table('option', schema=None) as batch_op:
        batch_op.create_unique_constraint(None, ['name'])

    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    with op.batch_alter_table('option', schema=None) as batch_op:
        batch_op.drop_constraint(None, type_='unique')

    with op.batch_alter_table('food_item', schema=None) as batch_op:
        batch_op.alter_column('type',
                              existing_type=sa.VARCHAR(length=64),
                              nullable=True)

    # ### end Alembic commands ###
