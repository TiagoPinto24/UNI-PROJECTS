extends Node

func _on_back_to_menu_pressed() -> void:
	get_tree().change_scene_to_file("res://Scenes/menu.tscn")
