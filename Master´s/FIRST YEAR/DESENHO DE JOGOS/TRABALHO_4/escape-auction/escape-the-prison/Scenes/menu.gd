extends Node

func _on_play_pressed() -> void:
	get_tree().change_scene_to_file("res://Scenes/PlayerNumbers.tscn")
	
func _on_howtoplay_pressed() -> void:
	get_tree().change_scene_to_file("res://Scenes/howtoplay.tscn")
	
func _on_quit_pressed() -> void:
	get_tree().quit()
