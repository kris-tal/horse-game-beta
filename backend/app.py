from flask import Flask
from flask_socketio import SocketIO

from config import Config
from database.models import db, socketio
from database.setup import initialize
from routes.horses import horses_bp
from routes.profile import profile_bp
from routes.auth import auth_bp
from flask_jwt_extended import JWTManager

from routes.shop import shop_bp
from routes.lobby import lobby_bp

app = Flask(__name__)
app.config.from_object(Config)
db.init_app(app)
socketio.init_app(app)
jwt = JWTManager(app)


# Register all the blueprints of the backend
app.register_blueprint(auth_bp) # Authorization blueprint
app.register_blueprint(profile_bp) # Profile management blueprint
app.register_blueprint(horses_bp) # Horse management blueprint
app.register_blueprint(shop_bp) # Shop management blueprint
app.register_blueprint(lobby_bp) # Lobby management blueprint

with app.app_context():
    db.create_all()
    initialize()
if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=4000)
