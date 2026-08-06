extends Node

func _on_back_pressed() -> void:
	get_tree().change_scene_to_file("res://Scenes/menu.tscn")

func _on_p_2_pressed() -> void:
	GameState.num_players = 2
	get_tree().change_scene_to_file("res://Scenes/main.tscn")

func _on_p_3_pressed() -> void:
	GameState.num_players = 3
	get_tree().change_scene_to_file("res://Scenes/main.tscn")

func _on_p_4_pressed() -> void:
	GameState.num_players = 4
	get_tree().change_scene_to_file("res://Scenes/main.tscn")
