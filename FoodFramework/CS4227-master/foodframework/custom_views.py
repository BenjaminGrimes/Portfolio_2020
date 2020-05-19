import os

from flask import flash, redirect, url_for, request
from flask_admin import BaseView, expose, AdminIndexView
from flask_admin.contrib.sqla import ModelView
from flask_login import current_user
from werkzeug.utils import secure_filename

from foodframework import app, db
from foodframework.forms import UploadForm
from foodframework.models import MenuFile


def is_accessible():
    '''
    Depending on the role of a user, they will be able to access Admin Privileges.
    These privileges allow Admins to add, delete and alter Table entries.
    These include: Customers, FoodItems, Options and Roles.
    Admins can also upload and select Menu PDF's
    '''
    is_admin = False

    for role in current_user.roles:
        if role.name == 'admin':
            is_admin = True
            break
    return current_user.is_authenticated and is_admin


class MenuFileView(BaseView):

    @expose('/', methods=('GET', 'POST'))
    def index(self):
        form = UploadForm()
        if form.validate_on_submit():
            f = form.file.data
            filename = secure_filename(f.filename)
            path = os.path.join(app.root_path, 'uploads', filename)
            f.save(path)
            path = os.path.join('uploads', filename)
            new_file = MenuFile(
                name=str(form.file_name.data), path_to_file=path)
            db.session.add(new_file)
            db.session.commit()
            flash(f'File uploaded', 'success')
        return self.render('/admin/file_upload.html', form=form)

    def is_accessible(self):
        return is_accessible()


class MyAdminIndexView(AdminIndexView):
    def is_accessible(self):
        return is_accessible()

    def inaccessible_callback(self, name, **kwargs):
        return redirect(url_for('login', next=request.url))

    @expose('/')
    def index(self):
        return self.render('admin/index.html')


class AdminView(ModelView):
    def is_accessible(self):
        return is_accessible()
