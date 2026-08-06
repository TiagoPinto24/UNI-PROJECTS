extends CharacterBody2D

@onready var mother: AnimatedSprite2D = %Mother
@onready var son_1: AnimatedSprite2D = %son1
@onready var son_2: AnimatedSprite2D = %son2
@onready var son_3: AnimatedSprite2D = %son3

const SPEED = 300.0

func _physics_process(delta):
	var direction = Vector2.ZERO

	if Input.is_action_pressed("right"):
		direction.x += 1
	if Input.is_action_pressed("left"):
		direction.x -= 1

	if Input.is_action_pressed("down"):
		direction.y += 1
	if Input.is_action_pressed("up"):
		direction.y -= 1

	direction = direction.normalized()

	velocity = direction * SPEED
	move_and_slide()

	if velocity.x != 0:
		var facing_left = velocity.x < 0
		mother.flip_h = facing_left
		son_1.flip_h = facing_left
		son_2.flip_h = facing_left
		son_3.flip_h = facing_left
