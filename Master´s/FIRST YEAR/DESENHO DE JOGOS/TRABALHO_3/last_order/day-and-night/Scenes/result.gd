extends Node

@onready var winner: Label = %Winner

@onready var red_truck_tex: Sprite2D = %red_truck
@onready var blue_truck_tex: Sprite2D = %blue_truck


func _ready() -> void:
	winner.text = GameData.winner_text

	var winner_sprite = Sprite2D.new()

	# Show the correct truck based on the winner
	if GameData.winner_text.begins_with("Red"):
		red_truck_tex.visible = true
	elif GameData.winner_text.begins_with("Blue"):
		blue_truck_tex.visible = true

	add_child(winner_sprite)

func _on_play_pressed() -> void:
	get_tree().change_scene_to_file("res://Scenes/game.tscn")

func _on_back_pressed() -> void:
	get_tree().change_scene_to_file("res://Scenes/menu.tscn")
