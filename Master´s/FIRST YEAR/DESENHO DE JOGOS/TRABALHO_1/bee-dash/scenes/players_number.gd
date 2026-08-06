extends Node

func _on_p1_pressed() -> void:
	start_game(1)

func _on_p2_pressed() -> void:
	start_game(2)

func _on_p3_pressed() -> void:
	start_game(3)

func _on_p4_pressed() -> void:
	start_game(4)


func start_game(num_players: int) -> void:
	var game_data = {
		"num_players": num_players,
		"current_player": 1,
		"results": [] 
	}
	
	var global = get_node("/root/Global")
	global.set_game_data(game_data)
	
	get_tree().change_scene_to_file("res://main.tscn")
	

func _on_back_to_menu_pressed() -> void:
	get_tree().change_scene_to_file("res://scenes/main_menu.tscn")
