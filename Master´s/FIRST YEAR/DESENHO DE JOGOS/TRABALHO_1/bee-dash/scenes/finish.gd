extends Area2D

func _on_body_entered(body: Node2D) -> void:
	if body.name == "CharacterBody2D":
		var game_manager = get_tree().get_first_node_in_group("game_manager")
		
		if game_manager:
			if game_manager.pots_collected >= game_manager.total_pots:
				game_manager.finish_run()
			else:
				game_manager.show_message(str(game_manager.pots_collected) + "/" + str(game_manager.total_pots) + " Pots Collected")
