/*
  Parallel Programming - 2026/2027

  Control algorithm interface
*/

void *detect_termination(void *);

void control_basic_start_hook(int);

void control_become_passive_hook(int);
void control_become_active_hook(int);

void control_basic_send_hook(int, int);
void control_basic_receive_hook(int, int);
