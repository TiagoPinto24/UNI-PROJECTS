extends Node

func _on_play_pressed() -> void:
	get_tree().change_scene_to_file("res://Scenes/PlayersNumber.tscn")
	
func _on_how_to_play_pressed() -> void:
	get_tree().change_scene_to_file("res://Scenes/HowToPlay.tscn")

func _on_leave_pressed() -> void:
	get_tree().quit()
