import json

from flask import Blueprint, jsonify
from database.models import User
from flask_jwt_extended import jwt_required, get_jwt_identity

from database.setup import DEFAULT_HORSE_TYPES
from managers.file_manager import create_user_files
from routes.shop import initialize_shop
from routes.horses import add_horse

profile_bp = Blueprint('profile', __name__)

def setup_new_account(user_id):
    try:
        create_user_files(user_id=user_id)
    except FileNotFoundError:
        initialize_shop()
        create_user_files(user_id=user_id)
    for horse in DEFAULT_HORSE_TYPES:
        if horse["default"]:
            add_horse(user_id, horse["id"])

@profile_bp.route('/profile', methods=['GET'])
@jwt_required()
def get_profile_data():
    identity = get_jwt_identity()
    user_data = json.loads(identity)
    user = User.query.filter_by(username=user_data["username"]).first()
    return jsonify({"username": user.username, "email": user.email, "money":user.money,
                    "prestige":user.prestige, "created_at": user.created_at}), 200 # OK