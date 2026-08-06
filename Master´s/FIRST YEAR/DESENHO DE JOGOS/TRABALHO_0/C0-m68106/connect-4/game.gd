extends Node2D

# CONSTANTS
const CELL_EMPTY = 0
const CELL_RED   = 1
const CELL_BLUE  = 2

const COLS = 7
const ROWS = 6
const WIN_LENGTH = 4

# NODES
@onready var buttons       = $GridContainer.get_children()
@onready var menu          = $Menu
@onready var label         = $Menu/Label
@onready var time_label    = $Turn

# Result Scenes
@onready var result_scene      = $Result
@onready var result_label      = $Result/Winner
@onready var play_again_button = $Result/Play
@onready var menu_button       = $Result/Back

# How To Play Scenes
@onready var how_to_play_scene = $HowToPlay
@onready var back_button       = $HowToPlay/Back  

# Variables
var current_player
var board

var red_icon  = preload("res://assets/red_ball.png")
var blue_icon = preload("res://assets/blue_ball.png")

# READY
func _ready():
	label.text = ""
	
	var button_index = 0
	for button in buttons:
		button.connect("pressed", _on_button_click.bind(button_index))
		button_index += 1

	# Connect the menu buttons
	$Menu/Play.connect("pressed", _on_play_pressed)
	$Menu/HowToPlay.connect("pressed", _on_how_to_play_pressed)
	$Menu/Leave.connect("pressed", _on_leave_pressed)
	
	# Connect the Result Buttons
	play_again_button.connect("pressed", _on_play_again_pressed)
	menu_button.connect("pressed", _on_result_menu_pressed)
	
	# Connect the HowToPlay buttons
	back_button.connect("pressed", _on_back_pressed)

	# Hide the other scenes
	result_scene.hide()
	how_to_play_scene.hide()
	
	reset_game()

# GRID BUTTONS
func _on_button_click(idx):
	var col = idx % COLS

	for row in range(ROWS - 1, -1, -1):
		if board[col][row] == CELL_EMPTY:
			board[col][row] = current_player
			_update_button(col, row)
			
			if check_win(col, row):
				result_label.text = ("🔴 Red" if current_player == CELL_RED else "🔵 Blue") + " Won!" + "\n" + "Congrats!!"
				result_scene.show()
				return
			elif check_fullboard():
				result_label.text = "Tie!"
				result_scene.show()
				return
			else:
				current_player = CELL_RED if current_player == CELL_BLUE else CELL_BLUE
				update_time_label()
			return

func _update_button(col, row):
	var idx = row * COLS + col
	var button = buttons[idx]
	button.icon = red_icon if current_player == CELL_RED else blue_icon

# Check Victory
func check_win(last_col, last_row):
	return (
		check_direction(last_col, last_row, 1, 0) or
		check_direction(last_col, last_row, 0, 1) or
		check_direction(last_col, last_row, 1, 1) or
		check_direction(last_col, last_row, 1, -1)
	)

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

func is_valid(col, row):
	return col >= 0 and col < COLS and row >= 0 and row < ROWS

func check_fullboard():
	for col in board:
		for cell in col:
			if cell == CELL_EMPTY:
				return false
	return true

# Reset Game
func reset_game():
	current_player = CELL_RED
	board = []
	for i in range(COLS):
		board.append([])
		for j in range(ROWS):
			board[i].append(CELL_EMPTY)
	for button in buttons:
		button.icon = null
	update_time_label()

func update_time_label():
	time_label.text = "🔴 Red Turn" if current_player == CELL_RED else "🔵 Blue Turn"

# Menu Buttons
func _on_play_pressed() -> void:
	label.text = ""
	reset_game()
	menu.hide()

func _on_how_to_play_pressed() -> void:
	menu.hide()
	how_to_play_scene.show()

func _on_leave_pressed() -> void:
	get_tree().quit()

# Result Buttons
func _on_play_again_pressed() -> void:
	result_scene.hide()
	reset_game()

func _on_result_menu_pressed() -> void:
	result_scene.hide()
	menu.show()

# How To Play Button
func _on_back_pressed() -> void:
	how_to_play_scene.hide()
	menu.show()
