from flask import Blueprint, request, jsonify

from database.models import HorseTypes, db
from managers.identity_manager import get_user
from flask_jwt_extended import jwt_required, get_jwt_identity
from managers.file_manager import get_user_file, DataType, create_default_file, save_user_file
from routes.horses import add_horse, upgrade_user_horse

shop_bp = Blueprint('shop', __name__)

# Function called to create a default user file
def initialize_shop():
    horses_to_buy = []
    upgrades = []

    for horse in HorseTypes.query.all():
        if horse.default:
            upgrades.append({
                "id": horse.id,
                "price": horse.price
            })
        else:
            # Not owned → available for purchase
            horses_to_buy.append({
                "id": horse.id,
                "price": horse.price
            })

    shop_data = {
        "horses": horses_to_buy,
        "upgrades": upgrades
    }

    create_default_file(DataType.Shop, shop_data)


# Get info about ones' stable
@shop_bp.route('/shop', methods=['GET'])
@jwt_required()
def shop_listing():
    identity = get_jwt_identity()
    user, status = get_user(identity)
    if status != 200:
        return user, status

    shop_data = get_user_file(user.id, DataType.Shop)
    return shop_data, 200

#Make purchase
@shop_bp.route('/shop', methods=['POST'])
@jwt_required()
def buy():
    identity = get_jwt_identity()
    user, status = get_user(identity)
    if status != 200:
        return user, status

    data = request.get_json()
    if not data or "id" not in data:
        return jsonify({"money":user.money, "success": False, "message": "Invalid query: missing id"}), 401

    horse_id = data["id"]

    # Load the user's shop file
    shop_data = get_user_file(user.id, DataType.Shop)

    if not shop_data:
        return jsonify({"success": False, "message": "Shop data not found"}), 404

    # --- 1) Check if horse is in "horses" (buy new) ---
    for horse in shop_data.get("horses", []):
        if horse["id"] == horse_id:
            # Check if funds are sufficient, purchase success is based on it
            if user.money < horse["price"]:
                return jsonify({"money":user.money, "success": False, "message": "Insufficient funds"}), 400

            user.money -= horse["price"]
            add_horse(user.id, horse_id)
            shop_data["horses"] = [h for h in shop_data["horses"] if h["id"] != horse_id]
            shop_data["upgrades"].append({
                "id": horse_id,
                "price": horse["price"]
            })
            db.session.commit()
            money = user.money
            save_user_file(user.id, DataType.Shop, shop_data)
            return jsonify({"money":money, "success": True, "message": "Horse purchased"}), 200

    # --- 2) Check if horse is in "upgrades" ---
    for upgrade in shop_data.get("upgrades", []):
        if upgrade["id"] == horse_id:
            if user.money < upgrade["price"]:
                return jsonify({"money":user.money, "success": False, "message": "Insufficient funds"}), 400
            user.money -= upgrade["price"]
            money = user.money
            upgrade_user_horse(user.id, horse_id)
            db.session.commit()
            return jsonify({"money":money, "success": True, "message": "Horse upgraded"}), 200

    # --- 3) Not found in either ---
    return jsonify({"money":user.money, "success": False, "message": "Invalid query: invalid id"}), 401


