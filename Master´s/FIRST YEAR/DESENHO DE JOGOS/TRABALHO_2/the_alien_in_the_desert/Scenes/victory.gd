extends Node

@onready var result_label : Label = $Result
@onready var characters = [
	$Characters/Character1,
	$Characters/Character2,
	$Characters/Character3,
	$Characters/Character4
]

func _ready():
	var index = Global.winner_index
	var texture = Global.winner_texture

	if index >= 0:
		result_label.text = "The Winner is Player " + str(index + 1) + "\nCONGRATS !!!"
	else:
		result_label.text = "Everybody died :(\nThere are no winners!"

	# Hide all characters first
	for c in characters:
		if is_instance_valid(c):
			c.visible = false

	# Show only the winning character
	if index >= 0 and index < characters.size():
		var winner_node = characters[index]
		if is_instance_valid(winner_node):
			winner_node.visible = true
			# Apply saved texture if available
			if Global.winner_texture != null and winner_node is Sprite2D:
				winner_node.texture = Global.winner_texture

func _on_back_to_menu_pressed() -> void:
	get_tree().change_scene_to_file("res://Scenes/menu.tscn")
