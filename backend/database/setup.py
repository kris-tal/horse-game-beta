import os

from database.models import db, HorseTypes
from managers.file_manager import CONTENT_DIR, DEFAULT_DATA_DIR, DEFAULT_FILES, DataType
from routes.shop import initialize_shop

DEFAULT_HORSE_TYPES = [
    {"id": 1, "price": 100, "default":True},
    {"id": 2, "price": 200, "default":False},
    {"id": 3, "price": 330, "default":False},
    {"id": 4, "price": 500, "default":False},
    {"id": 5, "price": 600, "default":False},
    {"id": 6, "price": 777, "default":False},
    {"id": 7, "price": 800, "default":False},
    {"id": 8, "price":1000, "default":False},
    {"id": 9, "price":1600, "default":False},
	{"id": 10, "price": 2004, "default":False},
	{"id": 11, "price": 4008, "default":False},
	{"id": 12, "price": 9999, "default":False}
]


def initialize():
    if not HorseTypes.query.first():
        for horse in DEFAULT_HORSE_TYPES:
            db.session.add(HorseTypes(**horse))
        db.session.commit()

    shop_path = os.path.join(CONTENT_DIR, DEFAULT_DATA_DIR, DEFAULT_FILES[DataType.Shop])
    if not os.path.exists(shop_path):
        initialize_shop()