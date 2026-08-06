extends Node

@onready var result_label: Label = $ResultLabel

func _ready() -> void:
	if Global.result == "win":
		result_label.text = "YOU WoN!\nCongrats!!"
	else:
		match Global.death_reason:
			"spikes":
				result_label.text = "You  Die!\nYou   were   impaled   by   the   spikes!"
			"wall":
				result_label.text = "You  Die!\nThe   wall   crushed   you!"
			_:
				result_label.text = "You Died..."

func _on_menu_pressed() -> void:
	get_tree().change_scene_to_file("res://Scenes/menu.tscn")

func _on_play_again_pressed() -> void:
	get_tree().change_scene_to_file("res://Scenes/main.tscn")
