extends Node

@onready var result_label: Label = $ResultLabel

func _ready() -> void:
	result_label.text = Global.result_text

func _on_playagain_pressed() -> void:
	get_tree().change_scene_to_file("res://Scenes/main.tscn")

func _on_menu_pressed() -> void:
	get_tree().change_scene_to_file("res://Scenes/menu.tscn")
