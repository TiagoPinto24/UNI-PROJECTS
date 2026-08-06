extends Node2D

# CONSTANTS
const CELL_EMPTY = 0
const CELL_RED   = 1
const CELL_BLUE  = 2

const COLS = 7
const ROWS = 6
const WIN_LENGTH = 4

# Drone movement
const DRONE_SPEED = 400.0
const DRONE_OFFSET_LEFT = 35
const DRONE_OFFSET_RIGHT = -35

var drone_direction = 1
var drone_col = 0

# NODES
@onready var buttons = $GridContainer.get_children()

@onready var red_turn: Label = %RedTurn
@onready var time_red: Label = %TimeRed
@onready var red_clock: Sprite2D = %RedClock

@onready var blue_turn: Label = $BluePlayer/BlueTurn
@onready var time_blue: Label = $BluePlayer/TimeBlue
@onready var blue_clock: Sprite2D = $BluePlayer/BlueClock

@onready var turn_timer: Timer = %TurnTimer

@onready var drone: Sprite2D = %drone
@onready var drone_mark: Marker2D = %DroneMark

# Game variables
var current_player
var board
var time_left = 10

# Preloaded piece scenes
var red_scene = preload("res://Scenes/RedBox.tscn")
var blue_scene = preload("res://Scenes/BlueBox.tscn")


func _ready():
	reset_game()
	turn_timer.connect("timeout", Callable(self, "_on_timer_tick"))


func _process(delta):
	_move_drone(delta)
	drone_mark.global_position = drone.global_position
	_update_drone_column()


# Moves the drone left and right automatically
func _move_drone(delta):
	drone.position.x += DRONE_SPEED * drone_direction * delta

	var first_rect = buttons[0].get_global_rect()
	var last_rect = buttons[COLS - 1].get_global_rect()

	var left_limit = first_rect.position.x + DRONE_OFFSET_LEFT
	var right_limit = last_rect.position.x + last_rect.size.x + DRONE_OFFSET_RIGHT

	# Reverse direction when reaching the edge
	if drone.global_position.x <= left_limit:
		drone.global_position.x = left_limit
		drone_direction = 1
	elif drone.global_position.x >= right_limit:
		drone.global_position.x = right_limit
		drone_direction = -1


# Updates which column the drone is above
func _update_drone_column():
	var closest_idx = 0
	var min_dist = INF

	for idx in range(COLS):
		var rect = buttons[idx].get_global_rect()
		var center_x = rect.position.x + rect.size.x / 2.0
		var dist = abs(center_x - drone_mark.global_position.x)

		if dist < min_dist:
			min_dist = dist
			closest_idx = idx

	drone_col = closest_idx


# Handles input (spacebar drops piece)
func _input(event):
	if event.is_action_pressed("ui_accept"):  # SPACE
		_drop_piece()


# Drops a piece in the current column
func _drop_piece():
	var col = drone_col

	for row in range(ROWS - 1, -1, -1):
		if board[col][row] == CELL_EMPTY:
			board[col][row] = current_player
			_update_button(col, row)

			# Check for win or draw
			if check_win(col, row):
				GameData.winner_text = ("Red Truck Won!\n" if current_player == CELL_RED else "Blue Truck Won!")
				get_tree().change_scene_to_file("res://Scenes/result.tscn")
				return

			elif check_fullboard():
				GameData.winner_text = "Tie!"
				get_tree().change_scene_to_file("res://Scenes/result.tscn")
				return

			# Switch player
			current_player = CELL_RED if current_player == CELL_BLUE else CELL_BLUE
			update_turn_ui()
			return


# Visually updates a cell with the right piece
func _update_button(col, row):
	var idx = row * COLS + col
	var button = buttons[idx]
	button.disabled = true  # Always disabled after placement

	if current_player == CELL_RED:
		var inst = red_scene.instantiate()
		add_child(inst)
		inst.global_position = button.global_position + button.size / 2
	else:
		var inst = blue_scene.instantiate()
		add_child(inst)
		inst.global_position = button.global_position + button.size / 2


# Checks all directions for a winning line
func check_win(last_col, last_row):
	return (
		check_direction(last_col, last_row, 1, 0) or
		check_direction(last_col, last_row, 0, 1) or
		check_direction(last_col, last_row, 1, 1) or
		check_direction(last_col, last_row, 1, -1)
	)


# Checks consecutive cells in one direction
func check_direction(col, row, dx, dy):
	var count = 1

	var c = col + dx
	var r = row + dy
	while is_valid(c, r) and board[c][r] == current_player:
		count += 1
		c += dx
		r += dy

	c = col - dx
	r = row - dy
	while is_valid(c, r) and board[c][r] == current_player:
		count += 1
		c -= dx
		r -= dy

	return count >= WIN_LENGTH


# Ensures coordinates are within the board
func is_valid(col, row):
	return col >= 0 and col < COLS and row >= 0 and row < ROWS


# Returns true if no empty cells remain
func check_fullboard():
	for col in board:
		for cell in col:
			if cell == CELL_EMPTY:
				return false
	return true


# Initializes a new game
func reset_game():
	current_player = CELL_RED
	board = []

	for i in range(COLS):
		board.append([])
		for j in range(ROWS):
			board[i].append(CELL_EMPTY)

	# Clear and disable all buttons
	for button in buttons:
		button.icon = null
		button.disabled = true

	update_turn_ui()


# Updates UI based on whose turn it is
func update_turn_ui():
	time_left = 10
	turn_timer.start()

	if current_player == CELL_RED:
		red_turn.visible = true
		time_red.visible = true
		red_clock.visible = true

		blue_turn.visible = false
		time_blue.visible = false
		blue_clock.visible = false
	else:
		red_turn.visible = false
		time_red.visible = false
		red_clock.visible = false

		blue_turn.visible = true
		time_blue.visible = true
		blue_clock.visible = true

	_update_time_label()


# Updates the countdown label
func _update_time_label():
	if current_player == CELL_RED:
		time_red.text = "00:%02d" % time_left
	else:
		time_blue.text = "00:%02d" % time_left


# Called every second by timer
func _on_timer_tick():
	time_left -= 1
	_update_time_label()

	# If time runs out, switch player
	if time_left <= 0:
		current_player = CELL_RED if current_player == CELL_BLUE else CELL_BLUE
		update_turn_ui()
