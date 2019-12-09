import os

from flask import render_template, flash, redirect, url_for, request, send_file
from flask_login import login_user, logout_user, current_user, login_required
from werkzeug.urls import url_parse
from werkzeug.utils import secure_filename

from app import app, db
from app.forms import LoginForm, RegistrationForm
from app.models import User, File
from config import SAVE_DIR


@app.route('/')
@app.route('/index')
@login_required
def index():
    return render_template('index.html', title='Home')


@app.route('/login', methods=['GET', 'POST'])
def login():
    if current_user.is_authenticated:
        return redirect(url_for('index'))
    form = LoginForm()
    if form.validate_on_submit():
        user = User.query.filter_by(username=form.username.data).first()
        if user is None or not user.check_password(form.password.data):
            flash('Invalid username or password')
            return redirect(url_for('login'))
        login_user(user, remember=form.remember_me.data)
        next_page = request.args.get('next')
        if not next_page or url_parse(next_page).netloc != '':
            next_page = url_for('index')
        return redirect(next_page)
    return render_template('login.html', title='Sign In', form=form)


@app.route('/logout')
def logout():
    logout_user()
    return redirect(url_for('index'))


@app.route('/register', methods=['GET', 'POST'])
def register():
    if current_user.is_authenticated:
        return redirect(url_for('index'))
    form = RegistrationForm()
    if form.validate_on_submit():
        user = User(username=form.username.data, email=form.email.data)
        user.set_password(form.password.data)
        db.session.add(user)
        db.session.commit()
        flash('Congratulations, you are now a registered user!')
        return redirect(url_for('login'))
    return render_template('register.html', title='Register', form=form)


@app.route('/upload', methods=['POST'])
@login_required
def upload():
    if request.files['file'].filename != '':
        file = request.files['file']
        filename = secure_filename(file.filename)
        dir = SAVE_DIR / current_user.username
        dir.mkdir(parents=True, exist_ok=True)
        file.save(str(dir / filename))

        db_entry = File(name=filename, user_id=current_user.id)
        db.session.add(db_entry)
        db.session.commit()
    return redirect(url_for('index'))


@app.route('/download/<path:filename>', methods=['GET'])
@login_required
def download(filename):
    dir = SAVE_DIR / current_user.username
    filepath = dir / filename
    print(f"FILEPATH={filepath}")
    return send_file(str(filepath), as_attachment=True)


@app.route('/delete/<path:filename>', methods=['GET'])
@login_required
def delete(filename):
    dir = SAVE_DIR / current_user.username
    filepath = dir / filename
    print(f"FILEPATH={filepath}")
    if filepath.exists():
        os.remove(str(filepath))
    db_entry = File.query.filter_by(name=filename).first()
    if db_entry is not None:
        db.session.delete(db_entry)
        db.session.commit()
    return redirect(url_for('index'))
