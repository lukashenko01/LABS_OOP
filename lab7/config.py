import os
from pathlib import Path

BASE_DIR = Path(os.path.dirname(__file__)).absolute()
SAVE_DIR = BASE_DIR / "upload_data"


class Config(object):
    SQLALCHEMY_DATABASE_URI = os.environ.get('DATABASE_URL') or \
                              'sqlite:///' + str(BASE_DIR / 'app.db')
    SQLALCHEMY_TRACK_MODIFICATIONS = False

    SECRET_KEY = os.environ.get('SECRET_KEY') or 'TOP_SECRET'
