from flask import Flask, render_template, request, jsonify
from flask_socketio import SocketIO, emit
import json
import os
import re
from datetime import datetime

app = Flask(__name__)
app.config['SECRET_KEY'] = 'super_chat_123'
socketio = SocketIO(app, cors_allowed_origins="*")

USER_FILE = 'users.json'
FRIEND_FILE = 'friends.json'
IP_LIMIT_FILE = 'ip_limit.json'
BAN_FILE = 'banned.json'

def load_json(path, default):
    if os.path.exists(path):
        with open(path, 'r', encoding='utf-8') as f:
            return json.load(f)
    return default

def save_json(path, data):
    with open(path, 'w', encoding='utf-8') as f:
        json.dump(data, f, ensure_ascii=False)

users = load_json(USER_FILE, {})
friends = load_json(FRIEND_FILE, {})
ip_limit = load_json(IP_LIMIT_FILE, {})
banned = load_json(BAN_FILE, {})  # 封号列表
requests = {}
online_users = {}
mute_users = {}  # 禁言列表

# 【这里设置管理员用户名】
ADMIN = "admin"

# ======================
# 注册
# ======================
@app.route('/reg', methods=['POST'])
def reg():
    user = request.form.get('user', '').strip()
    pwd = request.form.get('pwd', '').strip()
    ip = request.remote_addr

    if not user or not pwd:
        return jsonify({'ok':0, 'msg':'用户名或密码不能为空'})
    if len(user) < 2 or len(user) > 12:
        return jsonify({'ok':0, 'msg':'用户名长度 2-12 位'})
    if user.isdigit():
        return jsonify({'ok':0, 'msg':'用户名不能是纯数字'})
    if not re.match(r'^[a-zA-Z0-9_\u4e00-\u9fa5]+$', user):
        return jsonify({'ok':0, 'msg':'用户名不能包含特殊符号'})
    if len(pwd) < 4:
        return jsonify({'ok':0, 'msg':'密码至少4位'})
    if user in users:
        return jsonify({'ok':0, 'msg':'用户名已被注册'})

    today = datetime.now().strftime('%Y-%m-%d')
    if ip not in ip_limit:
        ip_limit[ip] = {'date': today, 'count': 0}
    ip_data = ip_limit[ip]
    if ip_data['date'] == today:
        if ip_data['count'] >= 2:
            return jsonify({'ok':0, 'msg':'当前IP今日注册已达上限'})
        ip_data['count'] += 1
    else:
        ip_limit[ip] = {'date': today, 'count': 1}
    save_json(IP_LIMIT_FILE, ip_limit)

    users[user] = pwd
    if user not in friends:
        friends[user] = []
    save_json(USER_FILE, users)
    save_json(FRIEND_FILE, friends)
    return jsonify({'ok':1, 'msg':'注册成功'})

# ======================
# 登录
# ======================
@app.route('/login', methods=['POST'])
def login():
    user = request.form.get('user')
    pwd = request.form.get('pwd')
    if user in banned:
        return jsonify({'ok':0, 'msg':'您已被封禁'})
    if users.get(user) == pwd:
        return jsonify({'ok':1, 'msg':'登录成功'})
    return jsonify({'ok':0, 'msg':'账号或密码错误'})

# ======================
# 管理员：禁言、踢人、封号
# ======================
@socketio.on('admin_action')
def admin_action(data):
    operator = data.get('admin')
    target = data.get('target')
    action = data.get('action')

    if operator != ADMIN:
        return

    if action == 'kick':
        if target in online_users:
            emit('kicked', {}, room=online_users[target])
    elif action == 'mute':
        mute_users[target] = True
        emit('msg_sys', f'【系统】{target} 已被禁言', broadcast=True)
    elif action == 'unmute':
        mute_users[target] = False
        emit('msg_sys', f'【系统】{target} 已解除禁言', broadcast=True)
    elif action == 'ban':
        banned[target] = True
        save_json(BAN_FILE, banned)
        if target in online_users:
            emit('banned', {}, room=online_users[target])
        emit('msg_sys', f'【系统】{target} 已被封号', broadcast=True)

# ======================
# 聊天
# ======================
@app.route('/')
def index():
    return render_template('index.html')

@socketio.on('join')
def join(username):
    online_users[username] = request.sid
    emit('user_data', {
        'online': list(online_users.keys()),
        'friends': friends.get(username, []),
        'requests': requests.get(username, []),
        'is_admin': (username == ADMIN)
    })
    emit('online_update', list(online_users.keys()), broadcast=True)

@socketio.on('public_msg')
def pub(data):
    u = data['from']
    if u in banned:
        return
    if mute_users.get(u, False):
        emit('msg_sys', '【系统】您已被禁言，无法发言')
        return
    emit('public_msg', data, broadcast=True)

@socketio.on('private_msg')
def priv(data):
    f = data['from']
    t = data['to']
    if f in banned or mute_users.get(f, False):
        return
    if t not in friends.get(f, []):
        return
    if t in online_users:
        emit('to_you_private', data, room=online_users[t])
    emit('from_you_private', data)

@socketio.on('add_friend')
def add(data):
    me = data['me']
    tar = data['target']
    if tar not in users:
        return
    if tar not in requests:
        requests[tar] = []
    if me not in requests[tar]:
        requests[tar].append(me)
    emit('friend_request', {'from': me}, room=online_users[tar])

@socketio.on('accept_friend')
def accept(data):
    me = data['me']
    you = data['you']
    if you in requests.get(me, []):
        requests[me].remove(you)
        if you not in friends[me]:
            friends[me].append(you)
        if me not in friends[you]:
            friends[you].append(me)
        save_json(FRIEND_FILE, friends)
    emit('user_data', {'friends': friends[me], 'requests': requests.get(me, [])})
    if you in online_users:
        emit('friend_accept', {'name': me}, room=online_users[you])

@socketio.on('disconnect')
def dc():
    bye = None
    for u, sid in online_users.items():
        if sid == request.sid:
            bye = u
            break
    if bye:
        del online_users[bye]
        emit('online_update', list(online_users.keys()), broadcast=True)

if __name__ == '__main__':
    socketio.run(app, host='0.0.0.0', port=5000, debug=True)
