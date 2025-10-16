import os
import shutil
from enum import Enum
import json

CONTENT_DIR = os.getenv("CONTENT_DIR", os.path.dirname(os.path.abspath(__file__)))

USER_DATA_DIR = "user_data"
DEFAULT_DATA_DIR = "default"

class DataType(Enum):
    Shop = 1

DEFAULT_FILES = {
    DataType.Shop: "shop.json"
}

def create_user_files(user_id:int):
    os.makedirs(USER_DATA_DIR, exist_ok=True)

    user_dir = os.path.join(CONTENT_DIR, USER_DATA_DIR, str(user_id))
    os.makedirs(user_dir, exist_ok=True)

    for filename in DEFAULT_FILES.values():
        file_path = os.path.join(user_dir, filename)
        default_file_path = os.path.join(CONTENT_DIR, DEFAULT_DATA_DIR, filename)
        if not os.path.exists(file_path):
            shutil.copy(default_file_path, file_path)

def get_user_file(user_id: int, file: DataType):
    user_dir = os.path.join(CONTENT_DIR, USER_DATA_DIR, str(user_id))
    file_name = DEFAULT_FILES[file]
    file_path = os.path.join(user_dir, file_name)

    if not os.path.exists(file_path):
        raise FileNotFoundError(f"File {file_name} for user {user_id} not found.")

    with open(file_path, "r") as f:
        data = json.load(f)
    return data

def save_user_file(user_id: int, file: DataType, data):
    user_dir = os.path.join(CONTENT_DIR, USER_DATA_DIR, str(user_id))
    os.makedirs(user_dir, exist_ok=True)  # Ensure directory exists

    file_name = DEFAULT_FILES[file]
    file_path = os.path.join(user_dir, file_name)

    with open(file_path, "w") as f:
        json.dump(data, f, indent=4)

def create_default_file(file: DataType, data):
    file_name = DEFAULT_FILES[file]
    default_dir = os.path.join(CONTENT_DIR, DEFAULT_DATA_DIR)
    os.makedirs(default_dir, exist_ok=True)
    file_path = os.path.join(default_dir, file_name)

    with open(file_path, "w") as f:
        json.dump(data, f, indent=4)

