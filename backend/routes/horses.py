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
            "training_points": horse.training_points,
            "longest_run_distance": horse.longest_run_distance
        } for horse in horses
    ]

    return jsonify(horses=horses_data), 200


@horses_bp.route('/horses/id/<int:id>', methods=['GET'])
@jwt_required()
def get_user_horse_by_id(id):
    identity = get_jwt_identity()
    data, status = get_user(identity)
    if status != 200:
        return data, status

    horse = Horse.query.filter_by(owner_id=data.id, id = id).first()
    if not horse:
        return jsonify({"msg":"Horse not found or belongs to a different user"}), 404
    return jsonify(
        {
            "id": horse.id,
            "name": horse.name_id,
            "level": horse.level,
            "training_points": horse.training_points,
            "longest_run_distance": horse.longest_run_distance
        }), 200


@horses_bp.route('/horses/name/<int:id>', methods=['GET'])
@jwt_required()
def get_user_horse_by_name(id):
    identity = get_jwt_identity()
    data, status = get_user(identity)
    if status != 200:
        return data, status

    horse = Horse.query.filter_by(owner_id=data.id, name_id = id).first()
    if not horse:
        return jsonify({"msg":"Horse not found or belongs to a different user"}), 404
    return jsonify(
        {
            "id": horse.id,
            "name": horse.name_id,
            "level": horse.level,
            "training_points": horse.training_points,
            "longest_run_distance": horse.longest_run_distance
        }), 200

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

@horses_bp.route('/reset-all-horses', methods=['POST'])
@jwt_required()
def reset_all_horses():
    """Reset all user's horses to level 0 for testing"""
    identity = get_jwt_identity()
    data, status = get_user(identity)
    if status != 200:
        return jsonify({
            "success":False,
            "msg":"Invalid user credentials!"
        }), status

    horses = Horse.query.filter_by(owner_id=data.id).all()
    for horse in horses:
        horse.level = 0
        horse.training_points = 0
    
    db.session.commit()
    
    return jsonify({
        "success": True,
        "msg": f"Reset {len(horses)} horses to level 0 with 0 training points"
    })

@horses_bp.route('/reset-horse', methods=['POST'])
@jwt_required()
def reset_horse():
    """Reset horse level and training points to defaults for testing"""
    identity = get_jwt_identity()
    data, status = get_user(identity)
    if status != 200:
        return jsonify({
            "success":False,
            "msg":"Invalid user credentials!"
        }), status

    query_data = request.get_json()
    horse_id = query_data["horseId"]
    horse = Horse.query.filter_by(id=horse_id, owner_id = data.id).first()
    if horse is None:
        return jsonify({
            "success": False,
            "msg": "Horse not found or belongs to a different user"
        }), 404

    # Reset to defaults
    horse.level = 0
    horse.training_points = 0
    db.session.commit()
    
    return jsonify({
        "success": True,
        "msg": "Horse reset to level 0 with 0 training points",
        "level": horse.level,
        "training_points": horse.training_points
    })

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
    horse_id = query_data["horseId"]
    distance = query_data.get("distance", 0)
    money = query_data.get("money", 0)
    success = query_data.get("success", True)
    longest_run_distance = query_data.get("longestRunDistance", 0)
    points_earned = query_data.get("pointsEarned", 0)
    
    horse = Horse.query.filter_by(id=horse_id, owner_id = data.id).first()
    if horse is None:
        return jsonify({
            "success": False,
            "msg": "Horse not found or belongs to a different user"
        }), 404

    data.money += money
    money = data.money
    
    
    if points_earned > 0:
        horse.training_points += points_earned
        if horse.training_points > 10000:
            horse.training_points = 10000

    if longest_run_distance > horse.longest_run_distance:
        horse.longest_run_distance = longest_run_distance
        print(f"New longest run record: {longest_run_distance}m")

    if success:
        print(f"Training completed: distance={distance}m, points_earned={points_earned}, total_points={horse.training_points}")
    else:
        print(f"Training failed: distance={distance}m, points_earned={points_earned}, total_points={horse.training_points}")
    
    level = horse.level  
    training_points = horse.training_points
    longest_run = horse.longest_run_distance
    print(f"Final state: level={level}, training_points={training_points}, longest_run={longest_run}m")

    db.session.commit()
    return jsonify({
        "success": True,
        "money": money,
        "training_points": training_points,
        "level": level,
        "longest_run_distance": longest_run
    })





