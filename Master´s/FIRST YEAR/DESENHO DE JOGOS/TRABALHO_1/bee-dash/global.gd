extends Node

var game_data = {
	"num_players": 1,        
	"current_player": 1,    
	"results": []            
}

func set_game_data(data: Dictionary) -> void:
	game_data = data

func add_result(time: float) -> void:
	game_data.results.append(time)

func next_player() -> bool:
	game_data.current_player += 1
	return game_data.current_player <= game_data.num_players

func get_winner_index() -> int:
	var min_time = INF            
	var winner_index = -1         
	for i in range(game_data.results.size()):
		if game_data.results[i] < min_time:
			min_time = game_data.results[i]
			winner_index = i
	return winner_index
