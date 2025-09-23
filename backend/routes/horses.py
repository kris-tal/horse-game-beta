import os

from flask import Blueprint, jsonify, request

from managers.identity_manager import get_user
from database.models import Horse, HorseTypes, db
from flask_jwt_extended import jwt_required, get_jwt_identity

horses_bp = Blueprint('horses', __name__)

# Get info about ones' horses
@horses_bp.route('/horses', methods=['GET'])
@jwt_required()
def get_user_horses():
    identity = get_jwt_identity()
    data, status = get_user(identity)
    if status != 200:
        return data, status

    horses = Horse.query.filter_by(owner_id=data.id).all()
    if not horses:
        return jsonify({}), 200
    horses_data = [
        {
            "id": horse.id,
            "name": horse.name_id,
            "level": horse.level,
            "training_points": horse.training_points
        } for horse in horses
    ]

    return jsonify(horses=horses_data), 200


@horses_bp.route('/horses/id/<int:id>', methods=['GET'])
@jwt_required()
def get_user_horse_by_id(horse_id):
    identity = get_jwt_identity()
    data, status = get_user(identity)
    if status != 200:
        return data, status

    horse = Horse.query.filter_by(owner_id=data.id, id = horse_id).first()
    if not horse:
        return jsonify({"msg":"Horse not found or belongs to a different user"}), 404
    return jsonify(
        {
            "id": horse.id,
            "name": horse.name_id,
            "level": horse.level,
            "training_points": horse.training_points
        }), 200


@horses_bp.route('/horses/name/<int:id>', methods=['GET'])
@jwt_required()
def get_user_horse_by_name(horse_id):
    identity = get_jwt_identity()
    data, status = get_user(identity)
    if status != 200:
        return data, status

    horse = Horse.query.filter_by(owner_id=data.id, name_id = horse_id).first()
    if not horse:
        return jsonify({"msg":"Horse not found or belongs to a different user"}), 404
    return jsonify(
        {
            "id": horse.id,
            "name": horse.name_id,
            "level": horse.level,
            "training_points": horse.training_points
        }), 200

# Helper functions (for shop functionalities)
def add_horse(user_id, name_id):
    new_horse = Horse(owner_id=user_id, name_id=name_id)
    db.session.add(new_horse)
    db.session.commit()
    return new_horse.id

def upgrade_user_horse(user_id, name_id):
    horse = Horse.query.filter_by(owner_id=user_id, name_id = name_id).first()
    if horse is None:
        return
    horse.level += 1
    db.session.commit()

@horses_bp.route('/train', methods=['POST'])
@jwt_required()
def finish_training():
    identity = get_jwt_identity()
    data, status = get_user(identity)
    if status != 200:
        return jsonify({
            "success":False,
            "msg":"Invalid user credentials!"
        }), status

    query_data = request.get_json()
    horse_id, money = query_data["horseId"], query_data["money"]
    horse = Horse.query.filter_by(id=horse_id, owner_id = data.id).first()
    if horse is None:
        return jsonify({
            "success": False,
            "msg": "Horse not found or belongs to a different user"
        }), 404

    data.money += money
    money = data.money
    if horse.training_points >= 10:#os.getenv("LEVELUP_TRAINING_SESSIONS"):
        horse.training_points = 0
        horse.level += 1
    else:
        horse.training_points += 1
    level = horse.level
    training_points = horse.training_points

    db.session.commit()
    return jsonify({
        "success": True,
        "money": money,
        "training_points": training_points,
        "level": level
    })





