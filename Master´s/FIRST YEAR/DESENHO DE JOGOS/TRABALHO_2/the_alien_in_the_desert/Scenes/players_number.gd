extends Node

func _on_back_to_menu_pressed() -> void:
	get_tree().change_scene_to_file("res://Scenes/menu.tscn")

func _on_play_pressed() -> void:
	Global.num_players = 1
	get_tree().change_scene_to_file("res://Scenes/main.tscn")

func _on_play_2_pressed() -> void:
	Global.num_players = 2
	get_tree().change_scene_to_file("res://Scenes/main.tscn")

func _on_play_3_pressed() -> void:
	Global.num_players = 3
	get_tree().change_scene_to_file("res://Scenes/main.tscn")

func _on_play_4_pressed() -> void:
	Global.num_players = 4
	get_tree().change_scene_to_file("res://Scenes/main.tscn")
