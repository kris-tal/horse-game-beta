from flask import Blueprint, request, jsonify

from database.models import HorseTypes, Horse, db
from managers.identity_manager import get_user
from flask_jwt_extended import jwt_required, get_jwt_identity
from managers.file_manager import get_user_file, DataType, create_default_file, save_user_file, create_user_files
from routes.horses import add_horse, upgrade_user_horse

shop_bp = Blueprint('shop', __name__)

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
            horses_to_buy.append({
                "id": horse.id,
                "price": horse.price
            })

    shop_data = {
        "horses": horses_to_buy,
        "upgrades": upgrades
    }

    create_default_file(DataType.Shop, shop_data)


@shop_bp.route('/shop', methods=['GET'])
@jwt_required()
def shop_listing():
    identity = get_jwt_identity()
    user, status = get_user(identity)
    if status != 200:
        return user, status

    owned = {h.name_id for h in Horse.query.filter_by(owner_id=user.id).all()}
    horses_to_buy = []
    upgrades = []
    for t in HorseTypes.query.all():
        entry = {"id": t.id, "price": t.price}
        if t.id in owned:
            upgrades.append(entry)
        else:
            horses_to_buy.append(entry)

    return {"horses": horses_to_buy, "upgrades": upgrades}, 200

@shop_bp.route('/shop', methods=['POST'])
@jwt_required()
def buy():
    identity = get_jwt_identity()
    user, status = get_user(identity)
    if status != 200:
        return user, status

    data = request.get_json()
    if not data or "id" not in data:
        return jsonify({"money": user.money, "success": False, "message": "Invalid query: missing id"}), 401

    horse_id = data["id"]
    t = HorseTypes.query.filter_by(id=horse_id).first()
    if not t:
        return jsonify({"money": user.money, "success": False, "message": "Invalid horse id"}), 404

    price = t.price
    owned = Horse.query.filter_by(owner_id=user.id, name_id=horse_id).first() is not None

    if user.money < price:
        return jsonify({"money": user.money, "success": False, "message": "Insufficient funds"}), 400

    user.money -= price
    if owned:
        upgrade_user_horse(user.id, horse_id)
        db.session.commit()
        return jsonify({"money": user.money, "success": True, "message": "Horse upgraded"}), 200
    else:
        add_horse(user.id, horse_id)
        db.session.commit()
        return jsonify({"money": user.money, "success": True, "message": "Horse purchased"}), 200


