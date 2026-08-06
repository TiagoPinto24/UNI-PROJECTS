extends Node

# =========================
# NODES
# =========================
@onready var blue_piece: Sprite2D = $Pieces/BluePiece
@onready var red_piece: Sprite2D = $Pieces/RedPiece
@onready var yellow_piece: Sprite2D = $Pieces/YellowPiece
@onready var green_piece: Sprite2D = $Pieces/GreenPiece

@onready var beans_label_1: Label = $player_huds/hud1/beans_number
@onready var beans_label_2: Label = $player_huds/hud2/beans_number
@onready var beans_label_3: Label = $player_huds/hud3/beans_number
@onready var beans_label_4: Label = $player_huds/hud4/beans_number

@onready var cards_left_label: Label = $CardsLeft

@onready var map_marker: Node = $MapMarker
@onready var draw_card_button: TextureButton = $DrawCard
@onready var card_sprite: Sprite2D = $CardSprite

@onready var trade_background: ColorRect = $TradeMenu/Background
@onready var trade_message_label: Label = $TradeMenu/LabelMessage

@onready var trade_yellow: TextureButton = $TradeMenu/Yellow
@onready var trade_green: TextureButton = $TradeMenu/Green
@onready var trade_blue: TextureButton = $TradeMenu/Blue
@onready var trade_red: TextureButton = $TradeMenu/Red

@onready var character_green: Sprite2D = $Characters/character1
@onready var character_yellow: Sprite2D = $Characters/character2
@onready var character_blue: Sprite2D = $Characters/character3
@onready var character_red: Sprite2D = $Characters/character4

@onready var scene_tree := get_tree()

# =========================
# CONSTANTS
# =========================
const RESULT_SCENE := "res://Scenes/Result.tscn"
const OFFSET := 16
const MOVE_TIME := 0.25
const CARDS_PATH := "res://Assets/cards/"
const MAX_BEANS := 7

const GAIN_BEAN_MARKS := [
	25, 9, 53, 5,
	59, 43, 11, 19,
	45, 29, 13, 33
]

const LOSE_BEAN_MARKS := [
	57, 41, 37, 21,
	27, 51, 35, 3,
	61, 49, 17, 1
]

# =========================
# VARIABLES
# =========================
var players := []
var current_player_index := 0
var player_positions: Dictionary[String, int] = {}
var marks := []
var is_moving := false

var deck: Array = []
var discard_pile: Array = []

var beans := {
	"Blue": 0,
	"Red": 0,
	"Yellow": 0,
	"Green": 0
}

var is_waiting_for_trade := false
var trade_source_player := ""

# Character sprite reference per player
var character_sprites := {}

# =========================
# READY
# =========================
func _ready() -> void:
	card_sprite.visible = true
	$TradeMenu.visible = false

	setup_players()
	load_marks()
	setup_pieces()
	create_deck()
	update_beans_labels()
	update_cards_left_label()

	setup_character_sprites()
	update_current_player_sprite()

# =========================
# SETUP
# =========================
func setup_players() -> void:
	players = [
		{"name": "Green", "piece": green_piece, "offset": Vector2(-OFFSET, OFFSET)},
		{"name": "Yellow", "piece": yellow_piece, "offset": Vector2(OFFSET, OFFSET)},
		{"name": "Blue", "piece": blue_piece, "offset": Vector2(-OFFSET, -OFFSET)},
		{"name": "Red", "piece": red_piece, "offset": Vector2(OFFSET, -OFFSET)}
	]

	for player in players:
		player_positions[player["name"]] = 0

func setup_character_sprites() -> void:
	character_sprites = {
		"Green": character_green,
		"Yellow": character_yellow,
		"Blue": character_blue,
		"Red": character_red
	}

	for sprite in character_sprites.values():
		sprite.visible = false

func update_current_player_sprite() -> void:
	for sprite in character_sprites.values():
		sprite.visible = false

	var current_player = players[current_player_index]
	var player_name: String = current_player["name"]

	if character_sprites.has(player_name):
		character_sprites[player_name].visible = true

func load_marks() -> void:
	marks.clear()
	for i in range(64):
		marks.append(map_marker.get_node("Mark" + str(i)))

func setup_pieces() -> void:
	for player in players:
		player["piece"].global_position = marks[0].global_position + player["offset"]

# =========================
# CARDS
# =========================
func create_deck() -> void:
	deck.clear()
	discard_pile.clear()

	var dir := DirAccess.open(CARDS_PATH)
	if dir == null:
		push_error("Cards folder not found")
		return

	dir.list_dir_begin()
	var file_name := dir.get_next()

	while file_name != "":
		if not dir.current_is_dir() and file_name.ends_with(".png"):
			deck.append({
				"texture": load(CARDS_PATH + file_name),
				"file_name": file_name
			})
		file_name = dir.get_next()

	dir.list_dir_end()
	deck.shuffle()
	update_cards_left_label()

func draw_card() -> Dictionary:
	if deck.is_empty():
		return {}
	var card: Dictionary = deck.pop_back()
	update_cards_left_label()
	return card

func update_cards_left_label() -> void:
	cards_left_label.text = str(deck.size())

# =========================
# MOVEMENT
# =========================
func get_move_amount(player_name: String) -> int:
	var bean_count: int = beans[player_name]
	if bean_count <= 3:
		return 3
	elif bean_count <= 5:
		return 2
	return 1

func _on_draw_card_pressed() -> void:
	if is_moving or is_waiting_for_trade:
		return

	is_moving = true
	draw_card_button.disabled = true

	var card := draw_card()
	if card.is_empty():
		Global.result_text = "Victory!\nCongratulations!!"
		scene_tree.change_scene_to_file(RESULT_SCENE)
		return

	show_card(card)

	var player = players[current_player_index]
	apply_card_effect(card, player)

	var steps := get_move_amount(player["name"])
	for i in range(steps):
		await move_one_step(player)

	var final_position := player_positions[player["name"]]
	apply_mark_effect(player["name"], final_position)

	discard_pile.append(card)

	if not is_waiting_for_trade:
		current_player_index = (current_player_index + 1) % players.size()
		draw_card_button.disabled = false
		update_current_player_sprite()

	is_moving = false

func move_one_step(player: Dictionary) -> void:
	var piece = player["piece"]
	var offset = player["offset"]
	var player_name: String = player["name"]

	var position := player_positions[player_name]
	position += 1

	if position > 63:
		position = 0

	player_positions[player_name] = position

	var target_position = marks[position].global_position + offset
	var tween := create_tween()
	tween.tween_property(piece, "global_position", target_position, MOVE_TIME)
	await tween.finished

# =========================
# MARK EFFECTS
# =========================
func apply_mark_effect(player_name: String, position: int) -> void:
	if position in GAIN_BEAN_MARKS:
		add_beans(player_name, 1)
	elif position in LOSE_BEAN_MARKS:
		add_beans(player_name, -1)

# =========================
# CARD EFFECTS
# =========================
func apply_card_effect(card: Dictionary, current_player: Dictionary) -> void:
	var file_name: String = card["file_name"]

	if file_name.contains("Spades"):
		add_beans(current_player["name"], 1)

	elif file_name.contains("Diamonds"):
		add_beans(current_player["name"], -1)

	elif file_name.contains("Clubs"):
		var shuffled_players = players.duplicate()
		shuffled_players.shuffle()
		for i in range(2):
			add_beans(shuffled_players[i]["name"], 1)
		for i in range(2, 4):
			add_beans(shuffled_players[i]["name"], -1)

	elif file_name.contains("Hearts"):
		show_trade_menu(current_player["name"])

	elif file_name.contains("Joker"):
		for player in players:
			add_beans(player["name"], 1)

# =========================
# BEANS
# =========================
func add_beans(player_name: String, amount: int) -> void:
	beans[player_name] = max(beans[player_name] + amount, 0)
	update_beans_labels()
	check_defeat()

func check_defeat() -> void:
	for player_name in beans.keys():
		if beans[player_name] >= MAX_BEANS:
			Global.result_text = "Defeat :(\nGood   luck   next   time!"
			scene_tree.change_scene_to_file(RESULT_SCENE)
			return

# =========================
# TRADE
# =========================
func show_trade_menu(current_player_name: String) -> void:
	is_waiting_for_trade = true
	trade_source_player = current_player_name

	$TradeMenu.visible = true
	trade_message_label.text = "Who do you want to give a bean to?"

	trade_yellow.disabled = current_player_name == "Yellow"
	trade_green.disabled = current_player_name == "Green"
	trade_blue.disabled = current_player_name == "Blue"
	trade_red.disabled = current_player_name == "Red"

func hide_trade_menu() -> void:
	is_waiting_for_trade = false
	$TradeMenu.visible = false

	current_player_index = (current_player_index + 1) % players.size()
	draw_card_button.disabled = false
	update_current_player_sprite()

func give_bean_to(target_player_name: String) -> void:
	if beans[trade_source_player] >= 1:
		add_beans(trade_source_player, -1)
		add_beans(target_player_name, 1)

	hide_trade_menu()

# =========================
# UI
# =========================
func show_card(card: Dictionary) -> void:
	card_sprite.texture = card["texture"]

func update_beans_labels() -> void:
	beans_label_1.text = str(beans["Green"])
	beans_label_2.text = str(beans["Yellow"])
	beans_label_3.text = str(beans["Blue"])
	beans_label_4.text = str(beans["Red"])

# =========================
# BUTTONS
# =========================
func _on_yellow_pressed() -> void:
	give_bean_to("Yellow")

func _on_green_pressed() -> void:
	give_bean_to("Green")

func _on_blue_pressed() -> void:
	give_bean_to("Blue")

func _on_red_pressed() -> void:
	give_bean_to("Red")
