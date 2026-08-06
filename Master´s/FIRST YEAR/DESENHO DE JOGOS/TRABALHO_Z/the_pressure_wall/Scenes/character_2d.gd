extends CharacterBody2D

@onready var sprite_2d: AnimatedSprite2D = $Sprite2D

const SPEED = 300.0
const JUMP_VELOCITY = -500.0

# Default gravity from project settings
var gravity = ProjectSettings.get_setting("physics/2d/default_gravity")

func _physics_process(delta):
	# Update animation based on horizontal movement
	if velocity.x > 1 or velocity.x < -1:
		sprite_2d.animation = "running"
	else:
		sprite_2d.animation = "default"

	# Apply gravity when the character is in the air
	if not is_on_floor():
		velocity.y += gravity * delta
		sprite_2d.animation = "jumping"

	# Jump input (only when on the floor)
	if Input.is_action_just_pressed("jump") and is_on_floor():
		velocity.y = JUMP_VELOCITY

	# Horizontal movement input
	var direction = Input.get_axis("left", "right")
	if direction:
		velocity.x = direction * SPEED
	else:
		velocity.x = move_toward(velocity.x, 0, SPEED)

	# Apply movement and collision
	move_and_slide()

	# Flip sprite based on movement direction
	sprite_2d.flip_h = velocity.x < 0

	
	
