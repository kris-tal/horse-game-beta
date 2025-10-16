import os

from flask_socketio import SocketIO
from flask_sqlalchemy import SQLAlchemy
from werkzeug.security import generate_password_hash, check_password_hash

db = SQLAlchemy()
socketio = SocketIO(cors_allowed_origins="*")

class User(db.Model):
    __tablename__ = 'users'
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(80), unique=True, nullable=False)
    email = db.Column(db.String(120), unique=False, nullable=True)
    password_hash = db.Column(db.String(128), nullable=False)
    created_at = db.Column(db.DateTime, server_default=db.func.now())
    money = db.Column(
        db.Integer,
        default=int(os.getenv("STARTUP_MONEY", "0"))
    )
    prestige = db.Column(db.Integer, default=0)
    def set_password(self, password):
        self.password_hash = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.password_hash, password)

# Model for a horse
class Horse(db.Model):
    __tablename__ = 'horses'
    id = db.Column(db.Integer, primary_key=True)
    name_id = db.Column(db.Integer, db.ForeignKey('horse_types.id'), nullable=False)
    level = db.Column(db.Integer, default=0)
    owner_id = db.Column(db.Integer, db.ForeignKey('users.id', ondelete='CASCADE'), nullable=False)
    owner = db.relationship('User', backref=db.backref('horses', cascade='all, delete'))
    training_points = db.Column(db.Integer, default=0)
    longest_run_distance = db.Column(db.Integer, default=0)

    def __repr__(self):
        return f'<Horse {self.name} ({self.level})>'

# Reference for horses one can purchase and maintain
class HorseTypes(db.Model):
    __tablename__ = 'horse_types'
    id = db.Column(db.Integer, primary_key=True)
    price = db.Column(db.Integer)
    default = db.Column(db.Boolean, default=False)