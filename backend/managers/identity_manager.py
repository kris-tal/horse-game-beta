import json

from flask import jsonify, Response

from database.models import User


def get_user(identity) -> tuple[User | Response, int]:
    user_data = json.loads(identity)
    user = User.query.filter_by(username=user_data["username"]).first()
    if not user:
        return jsonify({"msg": "Invalid session!"}), 401
    return user, 200

def get_decoded_data(username) -> User | None:
    if not username:
        return None
    user = User.query.filter_by(username=username).first()
    return user
