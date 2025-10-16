import json

from flask import Blueprint, request, jsonify

from database.models import db, User
from flask_jwt_extended import create_access_token
import re

from routes.profile import setup_new_account


def is_valid_email(email):
    pattern = r'^[\w\.-]+@[\w\.-]+\.\w{2,}$'
    return re.match(pattern, email) is not None

# These are only two endpoints which create access token and identity
auth_bp = Blueprint('auth', __name__)

@auth_bp.route('/signup', methods=['POST'])
def signup():
    data = request.get_json()

    if User.query.filter_by(username=data['username']).first():
        return jsonify({"msg": "Username already exists"}), 409

    if len(data['username']) > 80:
        return jsonify({"msg": "Username is too long!"}), 400

    user = User(username=data['username'])
    if 'email' in data:
        if not is_valid_email(data['email']):
            return jsonify({"msg": "Invalid email"}), 400
        user.email = data['email']
    user.set_password(data['password'])
    db.session.add(user)
    db.session.commit()

    setup_new_account(user.id)

    token = create_access_token(identity=json.dumps({"id": user.id, "username": user.username}))
    return jsonify({"token": token}), 201 # Created

@auth_bp.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    user = User.query.filter_by(username=data['username']).first()
    if user and user.check_password(data['password']):
        token = create_access_token(identity=json.dumps({"id": user.id, "username": user.username}))
        return jsonify({"token": token}), 200 # Login successful
    return jsonify({"msg": "Invalid credentials"}), 401 # Forbidden
@auth_bp.route('/', methods=['GET'])
def index():
    return jsonify({"msg": "Hello, cruel world!"}), 200
