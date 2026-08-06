extends Node

@onready var label_result: Label = $LabelResult
@onready var character_1: AnimatedSprite2D = $Character/Character1
@onready var character_2: AnimatedSprite2D = $Character/Character2
@onready var character_3: AnimatedSprite2D = $Character/Character3
@onready var character_4: AnimatedSprite2D = $Character/Character4

func _ready():
	show_winner()

func show_winner():
	var winner_id = GameState.race_winner_id
	label_result.text = "The   Winner   is   player   " + str(winner_id) + "!\nCongrats!!!"

	# Esconde todos os personagens
	character_1.visible = false
	character_2.visible = false
	character_3.visible = false
	character_4.visible = false

	match winner_id:
		1: character_1.visible = true
		2: character_2.visible = true
		3: character_3.visible = true
		4: character_4.visible = true

func _on_menu_pressed() -> void:
	get_tree().change_scene_to_file("res://Scenes/menu.tscn")
