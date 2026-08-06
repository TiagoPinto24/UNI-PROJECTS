extends Node

@onready var spikes: Node = $Spikes
@onready var gate: Area2D = $Gate
@onready var character: CharacterBody2D = $Character
@onready var wall: Area2D = $wall

# Wall movement settings
@export var wall_base_speed: float = 200.0
@export var wall_fast_multiplier: float = 1.8

# Wall fade settings
@export var wall_fade_time: float = 0.6
var wall_tween: Tween

# Pause menu references
@onready var pause_menu: Control = $PauseMenuLayer/PauseMenu
@onready var background: Panel = $PauseMenuLayer/PauseMenu/backgound
@onready var game_paused_label: Label = $PauseMenuLayer/PauseMenu/GamePausedLabel
@onready var resume: TextureButton = $PauseMenuLayer/PauseMenu/Resume
@onready var resumelabel: Label = $PauseMenuLayer/PauseMenu/Resume/Label
@onready var restart: TextureButton = $PauseMenuLayer/PauseMenu/Restart
@onready var restartlabel: Label = $PauseMenuLayer/PauseMenu/Restart/Label
@onready var menu: TextureButton = $PauseMenuLayer/PauseMenu/Menu
@onready var menulabel: Label = $PauseMenuLayer/PauseMenu/Menu/Label

var is_paused: bool = false

func _ready() -> void:
	# Initialize wall as invisible
	wall.visible = false
	wall.modulate.a = 0.0

	# Pause menu setup
	pause_menu.visible = false
	pause_menu.process_mode = Control.PROCESS_MODE_ALWAYS

	resume.process_mode = Control.PROCESS_MODE_ALWAYS
	restart.process_mode = Control.PROCESS_MODE_ALWAYS
	menu.process_mode = Control.PROCESS_MODE_ALWAYS

	# Button signals
	resume.pressed.connect(_on_resume_pressed)
	restart.pressed.connect(_on_restart_pressed)
	menu.pressed.connect(_on_menu_pressed)

	# Spike collision signals
	for spike in spikes.get_children():
		if spike is Area2D:
			spike.body_entered.connect(_on_spike_body_entered)

	# Gate and wall collision signals
	gate.body_entered.connect(_on_gate_body_entered)
	wall.body_entered.connect(_on_wall_body_entered)

func _process(delta: float) -> void:
	if Input.is_action_just_pressed("ui_cancel"):
		toggle_pause()

	if not is_paused:
		move_wall(delta)

# Toggles game pause state
func toggle_pause() -> void:
	is_paused = !is_paused
	get_tree().paused = is_paused
	pause_menu.visible = is_paused

# Moves the wall based on player movement
func move_wall(delta: float) -> void:
	var speed := wall_base_speed
	if character.velocity.x <= 0:
		speed *= wall_fast_multiplier
	wall.position.x += speed * delta

# Player loses when touching spikes
func _on_spike_body_entered(body: Node) -> void:
	if body == character:
		get_tree().paused = false
		Global.result = "lose"
		Global.death_reason = "spikes"
		get_tree().change_scene_to_file("res://Scenes/result.tscn")

# Player wins when reaching the gate
func _on_gate_body_entered(body: Node) -> void:
	if body == character:
		get_tree().paused = false
		Global.result = "win"
		Global.death_reason = ""
		get_tree().change_scene_to_file("res://Scenes/result.tscn")

# Player loses when caught by the wall
func _on_wall_body_entered(body: Node) -> void:
	if body == character:
		get_tree().paused = false
		Global.result = "lose"
		Global.death_reason = "wall"
		get_tree().change_scene_to_file("res://Scenes/result.tscn")

# Fades the wall in
func fade_wall_in() -> void:
	if wall_tween:
		wall_tween.kill()
	wall.visible = true
	wall.modulate.a = 0.0
	wall_tween = create_tween()
	wall_tween.tween_property(
		wall,
		"modulate:a",
		1.0,
		wall_fade_time
	).set_trans(Tween.TRANS_SINE).set_ease(Tween.EASE_OUT)

# Fades the wall out
func fade_wall_out() -> void:
	if wall_tween:
		wall_tween.kill()
	wall_tween = create_tween()
	wall_tween.tween_property(
		wall,
		"modulate:a",
		0.0,
		wall_fade_time
	).set_trans(Tween.TRANS_SINE).set_ease(Tween.EASE_IN)
	wall_tween.finished.connect(func():
		wall.visible = false
	)

# Controls wall visibility when entering/exiting black area
func _on_black_area_area_entered(area: Area2D) -> void:
	if area == wall:
		fade_wall_out()

func _on_black_area_area_exited(area: Area2D) -> void:
	if area == wall:
		fade_wall_in()

# Pause menu button callbacks
func _on_resume_pressed() -> void:
	toggle_pause()

func _on_restart_pressed() -> void:
	get_tree().paused = false
	get_tree().change_scene_to_file("res://Scenes/main.tscn")

func _on_menu_pressed() -> void:
	get_tree().paused = false
	get_tree().change_scene_to_file("res://Scenes/menu.tscn")
