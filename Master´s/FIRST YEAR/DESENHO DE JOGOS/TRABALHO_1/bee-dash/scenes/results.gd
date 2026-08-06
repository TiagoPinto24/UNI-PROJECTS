extends Node

@onready var line_edits = [
	$Control/GridContainer/Player,
	$Control/GridContainer/Time,
	$Control/GridContainer/Player1,
	$Control/GridContainer/Time1,
	$Control/GridContainer/Player2,
	$Control/GridContainer/Time2,
	$Control/GridContainer/Player3,
	$Control/GridContainer/Time3,
	$Control/GridContainer/Player4,
	$Control/GridContainer/Time4
]

@onready var winner: Label = %winner

func _ready() -> void:
	var global = get_node("/root/Global")
	var results = global.game_data.results
	
	var start_index = 2
	for i in range(results.size()):
		var grid_index = start_index + i * 2
		if grid_index + 1 < line_edits.size():
			line_edits[grid_index].text = "%d" % (i + 1)
			var minutes = int(results[i]) / 60
			var seconds = int(results[i]) % 60
			line_edits[grid_index + 1].text = "%02d:%02d" % [minutes, seconds]

	if results.size() > 0:
		var winner_index = 0
		var best_time = results[0]
		for i in range(1, results.size()):
			if results[i] < best_time:
				best_time = results[i]
				winner_index = i
		
		var winner_time_minutes = int(best_time) / 60
		var winner_time_seconds = int(best_time) % 60
		winner.text = "🏆 Winner: Player %d!" % [winner_index + 1]
	else:
		winner.text = "No results available."

func _on_button_pressed() -> void:
	get_tree().change_scene_to_file("res://scenes/main_menu.tscn")
