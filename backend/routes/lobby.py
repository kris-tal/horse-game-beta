import json
import string
import random
from enum import Enum

from flask_socketio import SocketIO, join_room, emit
from flask import current_app, request, Blueprint, jsonify
from flask_jwt_extended import jwt_required, get_jwt_identity, decode_token

from database.models import User, socketio
from managers.identity_manager import get_user, get_decoded_data

lobby_bp = Blueprint('lobby', __name__)

rooms = {}  # room_code -> { "creator": str, "status": RoomStatus, "players": [PlayerInfo], "winner": None }

class RoomStatus(Enum):
    Waiting = 1
    Started = 2
    Finished = 3

class PlayerStatus(Enum):
    Host = 1
    Player = 2

class PlayerInfo:
    def __init__(self, user_data: User, role: PlayerStatus, sid=None):
        self.role = role
        self.id = user_data.id
        self.username = user_data.username
        self.progress = 0
        self.sid = sid  # store socket session id

    def serialize(self):
        connected = False
        if self.sid:
            try:
                connected = socketio.server.manager.is_connected(self.sid, "/")
            except Exception:
                connected = False
        return {
            "username": self.username,
            "role": self.role.name,
            "progress": self.progress,
            "connected": connected,
        }

def log_lobby_state():
    summary = {
        code: {
            "status": room["status"].name,
            "players": [p.serialize() for p in room["players"]],
            "winner": room["winner"]
        }
        for code, room in rooms.items()
    }
    current_app.logger.info(f"Current lobby state: {json.dumps(summary, indent=2)}")

def log_and_emit(event, data=None, to=None, room=None, namespace=None, **kwargs):
    """Wrapper for socketio.emit that also logs the event."""
    current_app.logger.info(
        f"Emitting event='{event}' "
        f"to='{to or room or 'broadcast'}' "
        f"data={json.dumps(data, default=str)}"
    )
    return socketio.emit(event, data, to=to, room=room, namespace=namespace, **kwargs)


def get_code(length):
    return ''.join(random.choices(string.ascii_uppercase + string.digits, k=length))

def new_code():
    length = 6
    code = get_code(length)
    while code in rooms.keys():
        code = get_code(length)
    return code

# Create new user lobby
@lobby_bp.route('/lobby', methods=['POST'])
@jwt_required()
def create_room():
    identity = get_jwt_identity()
    data, status = get_user(identity)
    if status != 200:
        return data, status
    room_code = new_code()
    rooms[room_code] = {
        "created_by": identity,
        "status": RoomStatus.Waiting,
        "players": [PlayerInfo(data, PlayerStatus.Host)],
        "winner": None,
    }
    log_lobby_state()
    return jsonify({"room_code": room_code}), 200

@lobby_bp.route('/lobby/<code>/start', methods=['POST'])
@jwt_required()
def start_room(code):
    identity = get_jwt_identity()
    data, status = get_user(identity)
    if status != 200:
        return data, status

    room = rooms.get(code)
    if not room:
        return jsonify({"error": "Room not found"}), 404

    if room["created_by"] != identity:
        return jsonify({"error": "Only host can start the room"}), 403

    request_data = request.get_json()
    map_data = None

    if request_data and 'map_data' in request_data:
        map_data = request_data['map_data']
        # Log the efficiency improvement!
        if map_data:
            import json
            parsed = json.loads(map_data)
            if 'map_data' in parsed:
                current_app.logger.info(f"Compact map for room {code}: {len(parsed['map_data'])} chars")

    room["status"] = RoomStatus.Started

    emission_data = {
        "room": code,
        "map_data": map_data
    }

    log_and_emit("room_started", emission_data, to=code)

    return jsonify({"message": "Room started"}), 200

@socketio.on("connect")
def connect(auth):
    token = auth.get("token") if auth else None
    room = auth.get("room") if auth else None
    if token is None or room is None:
        return False

    decoded = decode_token(token)
    user = get_decoded_data(json.loads(decoded["sub"])["username"])
    if not user:
        return False

    if room not in rooms.keys() or rooms[room]["status"] != RoomStatus.Waiting:
        return False

    existing = next((p for p in rooms[room]["players"] if p.id == user.id), None)
    if existing:
        existing.sid = request.sid
        join_room(room)
        log_and_emit("player_joined", {"players": [p.serialize() for p in rooms[room]["players"]]}, to=room)
        #log_lobby_state()
        return True

    player = PlayerInfo(user, PlayerStatus.Player, sid=request.sid)
    rooms[room]["players"].append(player)
    join_room(room)
    log_and_emit("player_joined", {"players": [p.serialize() for p in rooms[room]["players"]]}, to=room)
    return True

@socketio.on("progress_update")
def progress_update(data):
    token = data.get("token")
    room_code = data.get("room")
    progress = data.get("progress", 0)

    decoded = decode_token(token)
    user = get_decoded_data(json.loads(decoded["sub"])["username"])
    room = rooms.get(room_code)

    if not user or not room or room["status"] != RoomStatus.Started:
        return

    for player in room["players"]:
        if player.username == user.username:
            player.progress = min(max(progress, 0), 3000)

    log_and_emit("progress_update", {"distances": [{"username": p.username, "progress": p.progress} for p in room["players"]]}, to=room_code)

    if room["winner"] is None:
        for player in room["players"]:
            if player.progress >= 3000:
                room["winner"] = player.id
                room["status"] = RoomStatus.Finished
                log_and_emit("game_won", {"winner": player.serialize()}, to=room_code)
                del room
                return


@socketio.on("disconnect")
def disconnect_handler():
    for room_code, room in list(rooms.items()):
        for player in room["players"]:
            if player.sid == request.sid:
                if player.role == PlayerStatus.Host:
                    log_and_emit("room_closed", {"room": room_code}, to=room_code)
                    del rooms[room_code]
                else:
                    room["players"].remove(player)
                    log_and_emit("player_left",
                                 {"players": [p.serialize() for p in room["players"]]}, to=room_code)
                    return
