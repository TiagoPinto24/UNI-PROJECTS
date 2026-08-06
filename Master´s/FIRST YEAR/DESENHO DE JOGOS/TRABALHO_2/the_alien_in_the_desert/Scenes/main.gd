extends Node2D

@onready var players = [
	$Characters/Character1,
	$Characters/Character2,
	$Characters/Character3,
	$Characters/Character4
]

@onready var buttonDice : TextureButton = $Buttons/ButtonDice
@onready var buttonAdvance2 : TextureButton = $Buttons/Advance2
@onready var buttonAdvanceX : TextureButton = $Buttons/AdvanceX

@onready var life_bars = [
	$TurnUI/PlayersBars/Player1Bar,
	$TurnUI/PlayersBars/Player2Bar,
	$TurnUI/PlayersBars/Player3Bar,
	$TurnUI/PlayersBars/Player4Bar
]

@onready var ui_sprites = [
	$TurnUI/Characters/Character1,
	$TurnUI/Characters/Character2,
	$TurnUI/Characters/Character3,
	$TurnUI/Characters/Character4
]

@onready var dice_faces = [
	$TurnUI/Dice/Dice1,
	$TurnUI/Dice/Dice2,
	$TurnUI/Dice/Dice3,
	$TurnUI/Dice/Dice4,
	$TurnUI/Dice/Dice5,
	$TurnUI/Dice/Dice6
]

var player_positions = [0, 0, 0, 0]
var player_lives = [2.0, 2.0, 2.0, 2.0]
var current_turn : int = 0
const FINAL_SLOT = 37
const HURRICANES_PER_PLAYER = 7
const HURRICANE_SCENE_PATH = "res://scenes/Hurricane.tscn"

var current_roll_value: int = 0

func _ready():
	buttonDice.pressed.connect(_on_button_pressed)
	buttonAdvance2.pressed.connect(_on_advance2_pressed)
	buttonAdvanceX.pressed.connect(_on_advancex_pressed)

	for i in range(life_bars.size()):
		life_bars[i].max_value = 2.0
		life_bars[i].value = player_lives[i]
		life_bars[i].step = 0.5
		life_bars[i].visible = false

	for i in range(players.size()):
		if players[i] is Sprite2D and ui_sprites[i] is Sprite2D:
			ui_sprites[i].texture = players[i].texture
		ui_sprites[i].visible = false
		players[i].z_index = 2
	
	for face in dice_faces:
		face.visible = false

	buttonAdvance2.visible = false
	buttonAdvanceX.visible = false

	# Place players at map start
	for i in range(players.size()):
		var marker_parent = get_node_or_null("MapMarkers/MapMarkers" + str(i + 1))
		if marker_parent == null:
			push_error("MapMarkers/MapMarkers" + str(i + 1) + " not found")
			continue

		var start_mark = marker_parent.get_node_or_null("Mark0")
		if start_mark:
			players[i].global_position = start_mark.global_position
			player_positions[i] = 0

	# Spawn hurricanes
	place_hurricanes_for_all_players()

	# Keep fog layers above hurricanes
	for i in range(players.size()):
		var fog_layer = get_node_or_null("FogLayers/FogLayer" + str(i + 1))
		if fog_layer:
			fog_layer.z_index = 1
			for fog_tile in fog_layer.get_children():
				if fog_tile is Control:
					fog_tile.mouse_filter = Control.MOUSE_FILTER_IGNORE

	for i in range(players.size()):
		update_fog_of_war(i)

	limit_active_players()
	update_turn_display()

func limit_active_players():
	var total_players = Global.num_players
	for i in range(players.size()):
		var active = i < total_players
		players[i].visible = active
		life_bars[i].visible = true 
		ui_sprites[i].visible = true 
		if not active:
			player_lives[i] = 0

# Spawn hurricanes for each player
func place_hurricanes_for_all_players():
	randomize()
	var hurricane_scene = load(HURRICANE_SCENE_PATH)

	for player_index in range(players.size()):
		var marker_parent = get_node_or_null("MapMarkers/MapMarkers" + str(player_index + 1))
		if marker_parent == null:
			push_error("MapMarkers/MapMarkers" + str(player_index + 1) + " not found")
			continue

		var chosen_slots = []
		while chosen_slots.size() < HURRICANES_PER_PLAYER:
			var pos = randi_range(1, FINAL_SLOT - 1)
			if pos not in chosen_slots:
				chosen_slots.append(pos)

		for slot in chosen_slots:
			var mark_name = "Mark" + str(slot)
			var mark = marker_parent.get_node_or_null(mark_name)
			if mark == null:
				continue

			var hurricane_instance = hurricane_scene.instantiate()
			hurricane_instance.global_position = mark.global_position
			hurricane_instance.name = "Hurricane_P" + str(player_index + 1) + "_M" + str(slot)
			hurricane_instance.z_index = 0
			add_child(hurricane_instance)

# Update fog visibility based on player position
func update_fog_of_war(player_index: int):
	var fog_parent = get_node_or_null("FogLayers/FogLayer" + str(player_index + 1))
	if fog_parent == null:
		return
	
	var current_pos = player_positions[player_index]
	for i in range(FINAL_SLOT + 1):
		var fog_tile = fog_parent.get_node_or_null("Fog" + str(i))
		if fog_tile:
			var distance = abs(i - current_pos)
			fog_tile.visible = distance > 1

func _on_button_pressed():
	if player_lives[current_turn] <= 0:
		_skip_to_next_alive_player()
		return

	buttonDice.disabled = true
	var roll = randi_range(1, 6)
	_show_dice_face(roll)
	current_roll_value = roll

	var advance_label = buttonAdvanceX.get_node("text") as Label
	if advance_label:
		advance_label.text = "Advance " + str(roll) + " spaces"

	buttonAdvance2.visible = true
	buttonAdvanceX.visible = true

func _on_advance2_pressed():
	await _advance_player(2)

func _on_advancex_pressed():
	await _advance_player(current_roll_value)

# Handle player movement
func _advance_player(steps: int):
	buttonAdvance2.visible = false
	buttonAdvanceX.visible = false

	for face in dice_faces:
		face.visible = false

	var player = players[current_turn]
	var path_parent = get_node_or_null("MapMarkers/MapMarkers" + str(current_turn + 1))
	if path_parent == null:
		push_error("MapMarkers/MapMarkers" + str(current_turn + 1) + " not found")
		return

	var current_pos = player_positions[current_turn]
	var target_pos = current_pos + steps
	if target_pos > FINAL_SLOT:
		target_pos = FINAL_SLOT

	await move_player(player, path_parent, current_pos, target_pos)
	player_positions[current_turn] = target_pos

	check_for_hurricane_hit(player, target_pos, current_turn)
	update_fog_of_war(current_turn)

	if player_lives[current_turn] <= 0:
		player.visible = false

	if target_pos >= FINAL_SLOT:
		show_winner(current_turn)
		return
		
	check_game_over_condition()
	_skip_to_next_alive_player()
	update_turn_display()
	buttonDice.disabled = false

# Show rolled dice face
func _show_dice_face(value: int):
	for i in range(dice_faces.size()):
		dice_faces[i].visible = (i == value - 1)

func move_player(player: Node2D, path_parent: Node, start: int, end: int) -> void:
	for i in range(start + 1, end + 1):
		if i > FINAL_SLOT:
			break

		var mark_name = "Mark" + str(i)
		var target_marker = path_parent.get_node_or_null(mark_name)
		if target_marker == null:
			break

		var current_x = player.global_position.x
		var target_x = target_marker.global_position.x
		if target_x < current_x:
			if player.has_method("set_flip_h"):
				player.set_flip_h(true)
			else:
				player.scale.x = -abs(player.scale.x)
		elif target_x > current_x:
			if player.has_method("set_flip_h"):
				player.set_flip_h(false)
			else:
				player.scale.x = abs(player.scale.x)

		var tween = create_tween()
		tween.tween_property(player, "global_position", target_marker.global_position, 0.4)
		await tween.finished
		await get_tree().create_timer(0.05).timeout

# Check hurricane collision
func check_for_hurricane_hit(player: Node2D, slot_index: int, player_index: int):
	for child in get_children():
		if child.name.begins_with("Hurricane_"):
			if child.global_position.distance_to(player.global_position) < 10.0:
				player_lives[player_index] -= 1.0
				player_lives[player_index] = clamp(player_lives[player_index], 0, 2)
				life_bars[player_index].value = player_lives[player_index]

# Skip to next alive player
func _skip_to_next_alive_player():
	var next_turn = current_turn
	for j in range(4):
		next_turn = (next_turn + 1) % 4
		if player_lives[next_turn] > 0:
			current_turn = next_turn
			return

func show_winner(player_index: int):
	Global.winner_index = player_index
	Global.winner_texture = players[player_index].texture

	buttonDice.disabled = true
	buttonAdvance2.visible = false
	buttonAdvanceX.visible = false
	for face in dice_faces:
		face.visible = false

	get_tree().change_scene_to_file("res://Scenes/Victory.tscn")

# Update UI turn indicators
func update_turn_display():
	for i in range(life_bars.size()):
		life_bars[i].visible = (i == current_turn)
		ui_sprites[i].visible = (i == current_turn)
	var label = $TurnUI.get_node_or_null("CurrentPlayerLabel")
	if label:
		label.text = "Player " + str(current_turn + 1)
		
func check_game_over_condition():
	var alive_count = 0
	for life in player_lives:
		if life > 0:
			alive_count += 1

	if alive_count == 0:
		Global.winner_index = -1
		Global.winner_texture = null
		get_tree().change_scene_to_file("res://Scenes/Victory.tscn")
