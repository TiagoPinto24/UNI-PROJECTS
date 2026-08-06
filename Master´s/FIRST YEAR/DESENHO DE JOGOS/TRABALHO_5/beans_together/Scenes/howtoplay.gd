extends Node


func _on_back_pressed() -> void:
	get_tree().change_scene_to_file("res://Scenes/menu.tscn")

func _on_next_pressed() -> void:
	get_tree().change_scene_to_file("res://Scenes/Howtoplay2.tscn")
