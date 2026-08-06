extends Node

# References to UI elements
@onready var time_label: Label = %TimeLabel
@onready var pots_label: Label = %PotsLabel
@onready var pots_left: Label = %PotsLeft
@onready var temporary_panel: Panel = %TemporaryPanel

# Game state variables
var elapsed_time := 0.0            
var pots_collected := 0           
var total_pots := 9               
var timer_running := true         

func _ready() -> void:
	# Initialize labels and hide temporary panel at the start
	update_pots_label()
	update_timer_label()
	temporary_panel.visible = false  

func _process(delta: float) -> void:
	# Update the timer every frame if the timer is running
	if timer_running:
		elapsed_time += delta
		update_timer_label()

func update_timer_label() -> void:
	var minutes = int(elapsed_time / 60)
	var seconds = int(elapsed_time) % 60
	time_label.text = str("\n%02d:%02d" % [minutes, seconds])

func add_pot() -> void:
	# Increment collected pots and update the UI
	pots_collected += 1
	update_pots_label()

func update_pots_label() -> void:
	# Update the pots label to show current collected count
	pots_label.text = "%d" % [pots_collected]

func add_time_penalty(seconds: float) -> void:
	# Add a time penalty and flash the timer label red
	elapsed_time += seconds
	update_timer_label()
	flash_timer_label_red()

func flash_timer_label_red() -> void:
	var original_color = time_label.modulate
	time_label.modulate = Color(1, 0, 0)
	await get_tree().create_timer(0.2).timeout
	time_label.modulate = original_color

func show_message(text: String, duration: float = 2.0) -> void:
	# Display a temporary message on the screen for a set duration
	temporary_panel.visible = true
	pots_left.text = text
	await get_tree().create_timer(duration).timeout
	pots_left.text = ""
	temporary_panel.visible = false

func finish_run() -> void:
	# Stop the timer and handle end-of-run logic
	timer_running = false

	var global = get_node("/root/Global")
	global.add_result(elapsed_time)  # Save the elapsed time

	if global.next_player():
		# If there is a next player, show a countdown and restart the scene
		temporary_panel.visible = true
		var next_player = global.game_data.current_player
		var countdown_time = 5  

		for i in range(countdown_time, -1, -1):
			pots_left.text = "Player  " + str(next_player) + "   is next in   \n" + str(i)
			await get_tree().create_timer(1.0).timeout

		get_tree().reload_current_scene()  
	else:
		# If no players left, go to results screen
		get_tree().change_scene_to_file("res://scenes/results.tscn")
