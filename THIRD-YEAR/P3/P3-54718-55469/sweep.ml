(*O valor 9 simboliza nao ter mina
usamos o 0 para marcar que certo quadrado tem 0 minas em su redor*)
(*Função que cria um tabuleiro vazio*)
let empty n = 
  Array.make_matrix n n 9

(*Função que coloca uma mina numa posição dada*)
let mine matrix r c =
  matrix.(r-1).(c-1) <- -1 (*-1 representa mina*)

(*Função que coloca no campo k minas aleatórias*)
let random matrix k =
  let n = Array.length matrix in
  let count_bombs = ref 0 in
  while !count_bombs < k do
    let r = (Random.int n) + 1 in
    let c = (Random.int n) + 1 in
    if matrix.(r-1).(c-1) <> -1 then
      mine matrix r c;
      count_bombs := !count_bombs + 1;
  done
  
(*Função que conta o numero de minas vizinhas*)
let count_neighbors matrix r c =
  let n = ref 0 in
  let rows = Array.length matrix in
  let cols = Array.length matrix.(0) in
  for x = r-1 to r+1 do
    for y = c-1 to c+1 do
      if (x >= 0 && x < rows && y >= 0 && y < cols) && matrix.(x).(y) = -1 then
        n := !n + 1;
    done
  done;
  !n

(*Função que vê o numero de minas vizinhas*)
let step matrixR matrixI r c =
  if matrixR.(r-1).(c-1) = -1 then
    matrixI.(r-1).(c-1) <- -2 (* -2 representa perder*)
  else
    matrixI.(r-1).(c-1) <- count_neighbors matrixR (r-1) (c-1)

(*Função que marca uma mina*)
let mark matrix r c =
  matrix.(r-1).(c-1) <- -1

(*Função que desmarca uma mina*)
let unmark matrix r c =
  matrix.(r-1).(c-1) <- 9

(*Função que verifica se o jogo acabou*)
let check_done matrixR matrixI =
  let n = Array.length matrixR in
  let count_bombs = ref 0 in
  let marked_bombs = ref 0 in
  for i = 0 to n - 1 do
    for j = 0 to n - 1 do
      if matrixI.(i).(j) = -1 then begin
        marked_bombs := !marked_bombs + 1;
        if matrixR.(i).(j) = -1 then
          count_bombs := !count_bombs + 1;
        end
    done;
  done;
  if !count_bombs = !marked_bombs then
    true
  else
    false

(*Função que faz print do tabuleiro*)
let dump matrix =
  let n = Array.length matrix in
  for i = 0 to n - 1 do
    print_string "(";
    for j = 0 to n - 1 do
      if matrix.(i).(j) = -1 then
        print_string "#";
      if matrix.(i).(j) = 9 then
        print_string " ";
      if matrix.(i).(j) >= 0 && matrix.(i).(j) < 9 then
        print_int matrix.(i).(j);
      if j <> n - 1 then
        print_string ","
    done;
    print_string ")";
    print_newline ()
  done

(*Função principal*)
let minesweeper () =
  print_endline "Insira um comando:";

  let input = ref "" in
  let inputSeparado = ref [||] in
  
  input := read_line ();
  inputSeparado := Array.of_list(String.split_on_char ' ' !input);

  (* Verifica se o comando criou um tabuleiro vazio, é necessario criar um tabuleiro vazio antes de prosseguir*)
  while !inputSeparado.(0) <> "empty" do
      print_endline "Instrução incorreta!\nTem de criar um tabuleiro vazio!\n";
      print_endline "Instrução:";
      input := read_line ();
      inputSeparado := Array.of_list (String.split_on_char ' ' !input);
    done;

  (*usamos 2 tabuleiros um para o campo real e outro para o campo que o utilizador vai ver e marcar bombas*)
  let campo_real = empty (int_of_string !inputSeparado.(1)) in
  let campo_imaginario = empty (int_of_string !inputSeparado.(1)) in
  print_endline "ok";

  (*Variavel que verifica se o jogo acabou, se for 1 o utilador ganhou, se for -1 o utilador perdeu*)
  let vitoria = ref 0 in
  
  while !vitoria = 0 do
    print_endline "Insira um comando:";
    input := read_line ();
    inputSeparado := Array.of_list (String.split_on_char ' ' !input);
    match !inputSeparado.(0) with
    | "empty" -> print_endline "Tabuleiro vazio já foi criado!\nPor favor insira um comando diferente!";

    | "random" -> 
      let k = int_of_string !inputSeparado.(1) in
      if (k < 0 || k <= Array.length campo_real * Array.length campo_real) then begin
      random campo_real k;
      print_endline "ok";
      end
      else
        print_endline "Quantidade de minas inválida!\n";

    | "mine" -> 
      let r = int_of_string !inputSeparado.(1) in
      let c = int_of_string !inputSeparado.(2) in
      mine campo_real r c;
      print_endline "ok";

    | "step" -> 
      step campo_real campo_imaginario (int_of_string !inputSeparado.(1)) (int_of_string !inputSeparado.(2));
      if campo_imaginario.((int_of_string !inputSeparado.(1))-1).((int_of_string !inputSeparado.(2))-1) = -2 then begin
        print_endline "Boom!";
        vitoria := -1
      end
      else begin
        let count = string_of_int(campo_imaginario.((int_of_string !inputSeparado.(1))-1).((int_of_string !inputSeparado.(2))-1)) in
        print_endline ("Count "^count);
      end

    | "mark" -> 
      mark campo_imaginario (int_of_string !inputSeparado.(1)) (int_of_string !inputSeparado.(2));
      print_endline "ok";
    
    | "unmark" -> 
      unmark campo_imaginario (int_of_string !inputSeparado.(1)) (int_of_string !inputSeparado.(2));

    | "done" -> 
      if check_done campo_real campo_imaginario then begin
        print_endline "ok";
        vitoria := 1
      end
      else
        print_endline "fail";

    | "dump" -> dump campo_imaginario;

    | _ -> print_endline "Instrução incorreta!\nPor favor insira um comando válido!";
  done;
  if !vitoria = 1 then
    true
  else
    false

let () =
  let finnish = minesweeper () in
  if finnish then
    print_endline "Vitoria!"
  else
    print_endline "Derrota!"