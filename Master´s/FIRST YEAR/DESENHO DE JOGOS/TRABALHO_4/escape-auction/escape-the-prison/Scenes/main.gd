extends Node

# --- Characters ---
@onready var character_1: AnimatedSprite2D = $Players/Character1
@onready var character_2: AnimatedSprite2D = $Players/Character2
@onready var character_3: AnimatedSprite2D = $Players/Character3
@onready var character_4: AnimatedSprite2D = $Players/Character4

@onready var gate_1: Sprite2D = $Gates/gate1
@onready var gate_2: Sprite2D = $Gates/gate2
@onready var gate_3: Sprite2D = $Gates/gate3
@onready var gate_4: Sprite2D = $Gates/gate4

# Energy bars
@onready var energy_1: TextureProgressBar = $Players/Character1/Energy1
@onready var energy_2: TextureProgressBar = $Players/Character2/Energy2
@onready var energy_3: TextureProgressBar = $Players/Character3/Energy3
@onready var energy_4: TextureProgressBar = $Players/Character4/Energy4

# Money labels
@onready var money_1: Label = $Players/Character1/money1
@onready var money_2: Label = $Players/Character2/money2
@onready var money_3: Label = $Players/Character3/money3
@onready var money_4: Label = $Players/Character4/money4

@onready var timer_label: Label = $CanvasLayer/Timer
@onready var auction_timer: Timer = $AuctionTimer

# Auction UI
@onready var color_rect: Panel = $CanvasLayer/Auction/backgoundAuction
@onready var auction_label: Label = $CanvasLayer/Auction/auctionLabel
@onready var auction_label_2: Label = $CanvasLayer/Auction/auctionLabel2
@onready var auction_label_3: Label = $CanvasLayer/Auction/auctionLabel3
@onready var auction_label_4: Label = $CanvasLayer/Auction/auctionLabel4
@onready var spin_box: SpinBox = $CanvasLayer/Auction/SpinBox
@onready var next_player: TextureButton = $CanvasLayer/Auction/NextPlayer
@onready var ff: TextureButton = $CanvasLayer/Auction/FF

# Dice nodes
@onready var dice_1: Node2D = $Players/Character1/Dice
@onready var dice_2: Node2D = $Players/Character2/Dice
@onready var dice_3: Node2D = $Players/Character3/Dice
@onready var dice_4: Node2D = $Players/Character4/Dice

# Pass sprites
@onready var ff_1: Sprite2D = $Players/Character1/Pass
@onready var ff_2: Sprite2D = $Players/Character2/Pass
@onready var ff_3: Sprite2D = $Players/Character3/Pass
@onready var ff_4: Sprite2D = $Players/Character4/Pass

# Camera settings
@onready var camera: Camera2D = $Camera2D
var camera_smooth_speed: float = 5.0
var camera_zoom_speed: float = 5.0
var camera_min_zoom: float = 0.25
var camera_max_zoom: float = 1.0
var camera_margin: float = 0.15

# Race settings
var speed_others: float = 130.0
var is_finished: bool = false

var energies := {1:100.0, 2:100.0, 3:100.0, 4:100.0}
var money := {1:10, 2:10, 3:10, 4:10}

# Auction data
var auction_active: bool = false
var auction_players: Array = []
var current_player_index: int = 0
var highest_bid: int = 0
var bids := {1:0, 2:0, 3:0, 4:0}
var auction_winner: int = 0

func _ready():
	randomize()
	setup_players()

	color_rect.visible = false
	auction_label.visible = false
	auction_label_2.visible = false
	auction_label_3.visible = false
	spin_box.visible = false
	next_player.visible = false
	ff.visible = false

	hide_all_ff_labels()

	auction_timer.wait_time = 3
	auction_timer.start()

	if spin_box:
		spin_box.connect("value_changed", Callable(self, "_on_SpinBox_value_changed"))
	if next_player:
		next_player.connect("pressed", Callable(self, "_on_NextPlayer_pressed"))
	if ff:
		ff.connect("pressed", Callable(self, "_on_FF_pressed"))
	if auction_timer:
		auction_timer.connect("timeout", Callable(self, "_on_AuctionTimer_timeout"))

func hide_all_ff_labels():
	ff_1.visible = false
	ff_2.visible = false
	ff_3.visible = false
	ff_4.visible = false

func _process(delta):
	if is_finished or auction_active:
		return

	var active = GameState.num_players

	if active >= 1:
		move_character(1, character_1, speed_others, delta, energy_1)
	if active >= 2:
		move_character(2, character_2, speed_others, delta, energy_2)
	if active >= 3:
		move_character(3, character_3, speed_others, delta, energy_3)
	if active >= 4:
		move_character(4, character_4, speed_others, delta, energy_4)

	update_camera(delta)

	if active >= 1:
		check_goal(character_1, gate_1)
	if active >= 2:
		check_goal(character_2, gate_2)
	if active >= 3:
		check_goal(character_3, gate_3)
	if active >= 4:
		check_goal(character_4, gate_4)

	var t = int(auction_timer.time_left)
	var minutes = t / 60
	var seconds = t % 60
	timer_label.text = "%02d:%02d" % [minutes, seconds]

func move_character(id: int, character: AnimatedSprite2D, speed: float, delta: float, energy_bar: TextureProgressBar) -> void:
	if energies.get(id, 0) <= 0:
		if character:
			character.play("default")
		return

	energies[id] -= 30 * delta
	energies[id] = max(energies[id], 0)
	if energy_bar:
		energy_bar.value = energies[id]

	character.position.x += speed * delta
	if character.animation != "running":
		character.play("running")

func check_goal(character: AnimatedSprite2D, gate: Node2D) -> void:
	if character.position.x >= gate.position.x:
		is_finished = true
		stop_all_characters()

		var winner_id: int = 0

		match character:
			character_1: winner_id = 1
			character_2: winner_id = 2
			character_3: winner_id = 3
			character_4: winner_id = 4

		GameState.race_winner_id = winner_id
		go_to_results()

func stop_all_characters():
	var active = GameState.num_players
	if active >= 1:
		character_1.play("default")
	if active >= 2:
		character_2.play("default")
	if active >= 3:
		character_3.play("default")
	if active >= 4:
		character_4.play("default")

func go_to_results():
	get_tree().change_scene_to_file("res://Scenes/Result.tscn")

# AUCTION SYSTEM

func _on_AuctionTimer_timeout():
	roll_dice_for_all_players()
	start_auction()

func roll_dice_for_all_players():
	var dice_nodes = {1:dice_1,2:dice_2,3:dice_3,4:dice_4}
	var active = GameState.num_players

	for id in range(1, active + 1):
		var roll = int(randi() % 6) + 1
		if dice_nodes.has(id) and dice_nodes[id]:
			show_dice_face(dice_nodes[id], roll)
		money[id] += roll

	update_money_labels()
	hide_all_ff_labels()

func start_auction():
	auction_active = true
	auction_timer.stop()
	stop_all_characters()

	color_rect.visible = true
	auction_label.visible = true
	auction_label_2.visible = true
	auction_label_3.visible = true
	auction_label_4.visible = true
	spin_box.visible = true
	next_player.visible = true
	ff.visible = true

	auction_players.clear()
	for i in range(1, GameState.num_players + 1):
		auction_players.append(i)

	current_player_index = 0
	highest_bid = 0

	for i in bids.keys():
		bids[i] = 0

	update_auction_ui()

func update_auction_ui():
	if auction_players.size() == 0:
		finish_auction()
		return

	current_player_index = clamp(current_player_index, 0, auction_players.size() - 1)

	var id = auction_players[current_player_index]
	auction_label_2.text = "Player " + str(id) + ", your offer:"
	auction_label_3.text = "Money: " + str(money.get(id, 0))

	var min_val = highest_bid + 1
	var max_val = money.get(id, 0)

	if max_val < min_val:
		spin_box.min_value = max_val
		spin_box.max_value = max_val
		spin_box.value = max_val
	else:
		spin_box.min_value = min_val
		spin_box.max_value = max_val
		spin_box.value = spin_box.min_value

	next_player.disabled = (money.get(id, 0) <= highest_bid)

	# --- Atualiza a label do maior lance ---
	auction_label_4.text = "Actual Bid: " + str(highest_bid)

func _on_SpinBox_value_changed(value):
	if auction_players.size() == 0:
		return

	var id = auction_players[current_player_index]

	if int(value) <= highest_bid or int(value) > money.get(id, 0):
		next_player.disabled = true
	else:
		next_player.disabled = false

func _on_NextPlayer_pressed():
	if auction_players.size() == 0:
		finish_auction()
		return

	var id = auction_players[current_player_index]
	var value = int(spin_box.value)

	if value <= highest_bid or value > money.get(id, 0):
		return

	bids[id] = value
	if value > highest_bid:
		highest_bid = value
		# Atualiza a label do maior lance imediatamente
		auction_label_4.text = "Actual Bid: " + str(highest_bid)

	advance_turn()

func _on_FF_pressed():
	if auction_players.size() == 0:
		finish_auction()
		return

	var id = auction_players[current_player_index]
	show_ff_label(id)

	auction_players.erase(id)

	# Atualiza a label do maior lance
	auction_label_4.text = "Actual Bid: " + str(highest_bid)

	if auction_players.size() == 0:
		finish_auction()
		return

	if current_player_index >= auction_players.size():
		current_player_index = 0

	if auction_players.size() == 1:
		finish_auction()
		return

	update_auction_ui()

func show_ff_label(id: int):
	match id:
		1: ff_1.visible = true
		2: ff_2.visible = true
		3: ff_3.visible = true
		4: ff_4.visible = true

func advance_turn():
	if auction_players.size() == 0:
		finish_auction()
		return

	current_player_index += 1
	if current_player_index >= auction_players.size():
		current_player_index = 0

	var id = auction_players[current_player_index]
	next_player.disabled = (money.get(id, 0) <= highest_bid)

	update_auction_ui()

func finish_auction():
	auction_active = false

	color_rect.visible = false
	auction_label.visible = false
	auction_label_2.visible = false
	auction_label_3.visible = false
	auction_label_4.visible = false
	spin_box.visible = false
	next_player.visible = false
	ff.visible = false

	if auction_players.size() == 0:
		auction_timer.start()
		return

	var winner_id: int = auction_players[0]
	var highest_local: int = bids.get(winner_id, 0)

	for id in auction_players:
		if bids.get(id, 0) > highest_local:
			highest_local = bids[id]
			winner_id = id

	var cost = bids.get(winner_id, 0)

	if cost > 0:
		money[winner_id] = max(0, money[winner_id] - cost)

	energies[winner_id] = 100.0

	update_money_labels()
	update_energy_bars()

	for id in auction_players:
		bids[id] = 0

	auction_timer.start()

func update_energy_bars():
	energy_1.value = energies[1]
	energy_2.value = energies[2]
	energy_3.value = energies[3]
	energy_4.value = energies[4]

func update_money_labels():
	money_1.text = str(money[1])
	money_2.text = str(money[2])
	money_3.text = str(money[3])
	money_4.text = str(money[4])

func show_dice_face(dice_node: Node, number: int):
	for i in range(1, 7):
		var face_path = "face_%d" % i
		if dice_node.has_node(face_path):
			var face = dice_node.get_node(face_path)
			face.visible = (i == number)

func update_camera(delta):
	if is_finished or auction_active:
		return

	var racers: Array = []
	var active = GameState.num_players

	if active >= 1 and character_1.visible:
		racers.append(character_1)
	if active >= 2 and character_2.visible:
		racers.append(character_2)
	if active >= 3 and character_3.visible:
		racers.append(character_3)
	if active >= 4 and character_4.visible:
		racers.append(character_4)

	if racers.size() == 0:
		return

	var min_x = racers[0].position.x
	var max_x = racers[0].position.x
	var min_y = racers[0].position.y
	var max_y = racers[0].position.y

	for r in racers:
		min_x = min(min_x, r.position.x)
		max_x = max(max_x, r.position.x)
		min_y = min(min_y, r.position.y)
		max_y = max(max_y, r.position.y)

	var bbox_width = max_x - min_x
	var bbox_height = max_y - min_y

	var target_center = Vector2((min_x + max_x) * 0.5, (min_y + max_y) * 0.5)

	var viewport_size: Vector2 = get_viewport().get_visible_rect().size

	if bbox_width <= 0.001:
		bbox_width = 50.0
	if bbox_height <= 0.001:
		bbox_height = 50.0

	var zoom_x = viewport_size.x / bbox_width
	var zoom_y = viewport_size.y / bbox_height

	var target_zoom_val = min(zoom_x, zoom_y)

	target_zoom_val = target_zoom_val * (1.0 - camera_margin)

	target_zoom_val = clamp(target_zoom_val, camera_min_zoom, camera_max_zoom)
	var target_zoom = Vector2(target_zoom_val, target_zoom_val)

	camera.position = camera.position.lerp(target_center, clamp(camera_smooth_speed * delta, 0.0, 1.0))
	camera.zoom = camera.zoom.lerp(target_zoom, clamp(camera_zoom_speed * delta, 0.0, 1.0))

func setup_players():
	var active_players = GameState.num_players

	if active_players < 4:
		character_4.visible = false
		energy_4.visible = false
		money_4.visible = false
		dice_4.visible = false
		ff_4.visible = false
		gate_4.visible = false

	if active_players < 3:
		character_3.visible = false
		energy_3.visible = false
		money_3.visible = false
		dice_3.visible = false
		ff_3.visible = false
		gate_3.visible = false

	auction_players.clear()
	for i in range(1, active_players + 1):
		auction_players.append(i)

	for i in range(active_players + 1, 5):
		energies[i] = 0
		money[i] = 0
