//variavel para ajudar na execução das operações o join e o leave
let viagemjoinLeave;
//variavel usada para nomear os botões de Join e Leave para cada viagem e poder alterá-los individualmente
let nviagem = 0;
//variavel usada para guardaros dados do search vindos do servidor
//usaremos essa variavel para posteriormente podermos aplicar filtros localmente, sem chamar o servidor novamente
let listaViagens;

//Função para validar a entrada e guardar o ID no sessionStorage
function validarEntrada(tipo) {
  //tipo 1 é no caso de condutor
  if (tipo == 1) {
    var loginId = document.getElementById("IDCondutor").value;
    if (!loginId) {
      alert("Please enter a Login ID.");
      return;
    }
    sessionStorage.setItem("loginId", loginId);
    console.log("Login ID saved in sessionStorage: " + loginId);
    window.location.href = "Condutor.html";
  }
  //tipo 2 é no caso de passageiro
  if (tipo == 2) {
    var loginId = document.getElementById("IDPassageiro").value;
    if (!loginId) {
      alert("Please enter a Login ID.");
      return;
    }
    sessionStorage.setItem("loginId", loginId);
    console.log("Login ID saved in sessionStorage: " + loginId);
    window.location.href = "Passageiro.html";
  }
}

//Função para resetar todos os campos
function Reset() {
  const textFields = document.querySelectorAll('input[type="text"]');
  textFields.forEach((field) => (field.value = ""));

  const dateFields = document.querySelectorAll('input[type="date"]');
  dateFields.forEach((field) => (field.value = ""));
}

//Função para fazer todas as operações no servidor
//aux é para receber algum valor que possa ser necessário
//tipo é um int que indica que operação vamos executar, usando um switch
function ServerOperacoes(aux, tipo) {
  const ADICIONA_PEDIDO = "https://magno.di.uevora.pt/tweb/t1/pedido/add";
  const ADICIONA_VIAGEM = "https://magno.di.uevora.pt/tweb/t1/viagem/add";
  const LISTA_VIAGENS = "https://magno.di.uevora.pt/tweb/t1/viagens/list";
  const LISTA_PEDIDOS = "https://magno.di.uevora.pt/tweb/t1/pedidos/list";
  const REMOVE_PEDIDO = "https://magno.di.uevora.pt/tweb/t1/pedido/remove";
  const REMOVE_VIAGEM = "https://magno.di.uevora.pt/tweb/t1/viagem/remove";
  const GET_CONDUTOR = "https://magno.di.uevora.pt/tweb/t1/condutor/get";
  const GET_PASSAGEIRO = "https://magno.di.uevora.pt/tweb/t1/passageiro/get";
  const PROCURA_VIAGEM = "https://magno.di.uevora.pt/tweb/t1/viagem/search";
  const ASSOCIAR_VIAGEM = "https://magno.di.uevora.pt/tweb/t1/viagem/join";
  const CANCELAR_VIAGEM = "https://magno.di.uevora.pt/tweb/t1/viagem/leave";

  switch (tipo) {
    //Adicionar uma nova viagem
    case 1:
      var condutor = sessionStorage.getItem("loginId");
      var localPartida = $("#localPartidaCond").val();
      var localChegada = $("#LocalChegadaCond").val();
      var data = $("#DataCond").val();

      //Verificar se todos os campos foram preenchidos
      if (localPartida == "" || localChegada == "" || data == "") {
        alert("Preencha todos os campos.");
        return;
      }

      $.ajax({
        url: ADICIONA_VIAGEM,
        method: "POST",
        data: {
          condutor: condutor,
          origem: localPartida,
          destino: localChegada,
          data: data,
        },
        success: function (response) {
          console.log(response);
          Reset();
        },
        error: function (xhr, status, error) {
          alert("Erro ao registar viagem: " + error);
          console.error(xhr, status, error);
        },
      });
      break;

    //Adicionar um novo pedido
    case 2:
      var passageiro = sessionStorage.getItem("loginId");
      var localPartida = $("#localPartidaPass").val();
      var localChegada = $("#localChegadaPass").val();
      var data = $("#DataPass").val();

      //Verificar se todos os campos foram preenchidos
      if (localPartida == "" || localChegada == "" || data == "") {
        alert("Preencha todos os campos.");
        return;
      }

      $.ajax({
        url: ADICIONA_PEDIDO,
        method: "POST",
        data: {
          passageiro: passageiro,
          origem: localPartida,
          destino: localChegada,
          data: data,
        },
        success: function (response) {
          console.log(response);
          Reset();
        },
        error: function (xhr, status, error) {
          alert("Erro ao registar viagem: " + error);
          console.error(xhr, status, error);
        },
      });
      break;

    //Listar viagens
    case 3:
      $.ajax({
        url: LISTA_VIAGENS,
        type: "GET",
        success: function (response) {
          if (response.viagens) {
            $("#itemList").empty();
            //Percorrer todas as viagens
            $.each(response.viagens, function (index, viagem) {
              const adicionarviagem = `ID: ${viagem.v_id}<br>
                                                Condutor: ${viagem.condutor}<br>
                                                Origem: ${
                                                  viagem.origem.place
                                                }<br>
                                                Destino: ${
                                                  viagem.destino.place
                                                }<br>
                                                Data: ${viagem.data}<br>
                                                Passageiros: ${viagem.passageiros.join(
                                                  ", "
                                                )}<br><br>`;

              $("#itemList").append("<li>" + adicionarviagem + "</li>");
            });
          } else $("#itemList").append("<li>Não há nenhuma viagem</li>");
        },
        error: function (xhr, status, error) {
          console.error("Erro em aceder á lista", status, error);
          $("#itemList").append("<li>Erro.</li>");
        },
      });
      break;

    //Listar pedidos
    case 4:
      $.ajax({
        url: LISTA_PEDIDOS,
        type: "GET",
        success: function (response) {
          if (response.pedidos) {
            $("#itemList").empty();
            //Percorrer todos os pedidos
            $.each(response.pedidos, function (index, pedido) {
              const adicionarPedido = `ID: ${pedido.p_id}<br>
                                                Passageiro: ${pedido.passageiro}<br>
                                                Origem: ${pedido.origem.place}<br>
                                                Destino: ${pedido.destino.place}<br>
                                                Data: ${pedido.data}<br>
                                                Condutor: ${pedido.condutor}<br><br>`;

              $("#itemList").append("<li>" + adicionarPedido + "</li>");
            });
          } else $("#itemList").append("<li>Não há nenhum Pedido</li>");
        },
        error: function (xhr, status, error) {
          console.error("Erro em Aceder á Lista", status, error);
          $("#itemList").append("<li>Erro.</li>");
        },
      });
      break;

    //Remover viagem
    case 5:
      var condutor = sessionStorage.getItem("loginId");
      var viagemID = $("#IDViagem").val();

      //Verificar se o campo foi preenchido
      if (viagemID == "") {
        alert("Preencha o ID da viagem.");
        return;
      }

      $.ajax({
        url: REMOVE_VIAGEM,
        method: "POST",
        data: {
          v_id: viagemID,
          condutor: condutor,
        },
        success: function (response) {
          console.log(response);
          Reset();
        },
        error: function (xhr, status, error) {
          alert("Erro ao remover viagem" + error);
          console.error(xhr, status, error);
        },
      });
      break;

    //Remover pedido
    case 6:
      var passageiro = sessionStorage.getItem("loginId");
      var pedidoID = $("#IDPedido").val();

      //Verificar se o campo foi preenchido
      if (pedidoID == "") {
        alert("Preencha o ID do pedido.");
        return;
      }

      $.ajax({
        url: REMOVE_PEDIDO,
        method: "POST",
        data: {
          p_id: pedidoID,
          passageiro: passageiro,
        },
        success: function (response) {
          console.log(response);
          Reset();
        },
        error: function (xhr, status, error) {
          alert("Erro ao remover viagem" + error);
          console.error(xhr, status, error);
        },
      });
      break;

    //Informação sobre os passageiros, para os condutores
    case 7:
      //ID do condutor que se quer informação
      var toCondutor = $("#IDToCondutor").val();

      //Verificar se o campo foi preenchido
      if (toCondutor == "") {
        alert("Preencha com um ID de um condutor.");
        return;
      }

      $.ajax({
        url: GET_PASSAGEIRO + "/" + toCondutor,
        method: "GET",
        success: function (response) {
          $("#itemList").empty();
          if (response.result) {
            const fiabilidade = `Pontuação de Fiabilidade: ${response.result.fiabilidade}<br><br>`;
            $("#itemList").append("<li>" + fiabilidade + "</li>");

            //Separar as ultimas 10 viagens
            const ultimasViagens = response.result.viagens.slice(0, 10);
            //Percorrer as ultimas 10 viagens
            $.each(ultimasViagens, function (index, pedido) {
              const viagemInfo = `Origem: ${pedido.origem.place}<br>
                                                Destino: ${pedido.destino.place}<br>
                                                Data: ${pedido.data}<br>
                                                Condutor: ${pedido.condutor}<br><br>`;

              $("#itemList").append("<li>" + viagemInfo + "</li>");
            });
          } else
            $("#itemList").append("<li>Não existe passageiro com esse ID</li>");
          console.log(response);
          Reset();
        },
      });
      break;

    //Informação sobre os condutores, para os passageiros
    case 8:
      //ID do passageiro que se quer informação
      var toPassageiro = $("#IDToCondutor").val();

      //Verificar se o campo foi preenchido
      if (toPassageiro == "") {
        alert("Preencha com um ID de um passageiro.");
        return;
      }

      $.ajax({
        url: GET_CONDUTOR + "/" + toPassageiro,
        method: "GET",
        success: function (response) {
          $("#itemList").empty();
          if (response.result) {
            $("#itemList").empty();
            const fiabilidade = `Pontuação de Fiabilidade: ${response.result.fiabilidade}<br><br>`;
            $("#itemList").append("<li>" + fiabilidade + "</li>");

            //Separar as ultimas 10 viagens
            const ultimasViagens = response.result.viagens.slice(0, 10);
            //Percorrer as ultimas 10 viagens
            $.each(ultimasViagens, function (index, viagem) {
              const viagemInfo = `Origem: ${viagem.origem.place}<br>
                                                Destino: ${
                                                  viagem.destino.place
                                                }<br>
                                                Data: ${viagem.data}<br>
                                                Passageiros: ${viagem.passageiros.join(
                                                  ", "
                                                )}<br><br>`;

              $("#itemList").append("<li>" + viagemInfo + "</li>");
            });
          } else
            $("#itemList").append("<li>Não existe passageiro com esse ID</li>");
          console.log(response);
          Reset();
        },
      });
      break;

    //Procurar uma viagem com filtros, pelo menos um dos filtros tem que estar preenchido
    case 9:
      var passageiro = sessionStorage.getItem("loginId");
      var origemFiltros = $("#OrigemToFiltros").val();
      var destinoFiltros = $("#DestinoToFiltros").val();
      var dataFiltros = $("#DataToFiltros").val();
      //os botões começam no 0
      nviagem = 0;

      //data, pelo menos um dos campos tem de estar preenchido
      let requestData = {};
      if (origemFiltros) requestData.origem = origemFiltros;
      if (destinoFiltros) requestData.destino = destinoFiltros;
      if (dataFiltros) requestData.data = dataFiltros;
      if (origemFiltros == "" && destinoFiltros == "" && dataFiltros == "") {
        alert("Preencha pelo menos um campos.");
        return;
      }

      $.ajax({
        url: PROCURA_VIAGEM,
        type: "POST",
        data: requestData,
        success: function (response) {
          if (response.viagens) {
            $("#itemList").empty();
            //Percorrer as viagens resultantes da chamada do server
            $.each(response.viagens, function (index, viagem) {
              const viagemInfo = `ID: ${viagem.v_id}<br>
                                                Condutor: ${viagem.condutor}<br>
                                                Origem: ${
                                                  viagem.origem.place
                                                }<br>
                                                Destino: ${
                                                  viagem.destino.place
                                                }<br>
                                                Data: ${viagem.data}<br>
                                                Passageiros: ${viagem.passageiros.join(
                                                  ", "
                                                )}<br>`;

              $("#itemList").append("<li>" + viagemInfo);
              //guardar na variavel join o id da viagem, para aplicar a operação correspondente
              viagemjoinLeave = viagem.v_id;

              //Verificar se o passageiro esta na viagem e adicionar os botões caso esteja ou não
              if (viagem.passageiros.includes(passageiro)) {
                $("#itemList").append(
                  '<button id="botaoDeixar' +
                    nviagem +
                    '" type="button" onclick="ServerOperacoes(' +
                    nviagem +
                    ',11)">Deixar viagem</button>'
                );
                $("#itemList").append(
                  '<button id="botaoJuntar' +
                    nviagem +
                    '" style="display: none" type="button" onclick="ServerOperacoes(' +
                    nviagem +
                    ',10)">Juntar-se</button>'
                );
                $("#itemList").append("<br><br></li>");
              } else {
                $("#itemList").append(
                  '<button id="botaoJuntar' +
                    nviagem +
                    '" type="button" onclick="ServerOperacoes(' +
                    nviagem +
                    ',10)">Juntar-se</button>'
                );
                $("#itemList").append(
                  '<button id="botaoDeixar' +
                    nviagem +
                    '" style="display: none" type="button" onclick="ServerOperacoes(' +
                    nviagem +
                    ',11)">Deixar viagem</button>'
                );
                $("#itemList").append("<br><br></li>");
              }
              //incremento para o proximo botão
              nviagem++;
            });

            listaViagens = response.viagens;
            document.getElementById("serverButton").style.display = "none";
            document.getElementById("FiltrosButton").style.display = "";
          } else $("#itemList").append("<li>Não há nenhuma viagem</li>");
          Reset();
        },
        error: function (xhr, status, error) {
          console.error("Erro em aceder á lista", status, error);
          $("#itemList").append("<li>Erro.</li>");
        },
      });
      break;

    //Juntar-se a uma viagem
    case 10:
      var passageiro = sessionStorage.getItem("loginId");
      var IDViagem = viagemjoinLeave;

      $.ajax({
        url: ASSOCIAR_VIAGEM,
        method: "POST",
        data: {
          v_id: IDViagem,
          passageiro: passageiro,
        },
        success: function (response) {
          //trocar os botões de Juntar-se para Deixar viagem
          document.getElementById("botaoJuntar" + aux).style.display = "none";
          document.getElementById("botaoDeixar" + aux).style.display = "";
          alert("Associado à viagem com sucesso!");
          console.log(response);
        },
        error: function (response) {
          alert("Erro ao associar-se à viagem.");
          console.log(response);
        },
      });
      break;

    //Deixar-se de uma viagem
    case 11:
      var passageiro = sessionStorage.getItem("loginId");
      var IDViagem = viagemjoinLeave;

      $.ajax({
        url: CANCELAR_VIAGEM,
        method: "POST",
        data: {
          v_id: IDViagem,
          passageiro: passageiro,
        },
        success: function (response) {
          //trocar os botões de Deixar viagem para Juntar-se
          document.getElementById("botaoDeixar" + aux).style.display = "none";
          document.getElementById("botaoJuntar" + aux).style.display = "";
          alert("Deixou de fazer parte desta viagem");
          console.log(response);
        },
        error: function (response) {
          alert("Erro ao desassociar-se à viagem.");
          console.log(response);
        },
      });
      break;

    default:
      break;
  }
}

//Função para aplicar os filtros localmente, sem usar o servidor
function Filtros() {
  //no caso de não haver filtros, mostrar todas as viagens
  var origem = document.getElementById("OrigemToFiltros").value;
  var destino = document.getElementById("DestinoToFiltros").value;
  var data = document.getElementById("DataToFiltros").value;
  var passageiro = sessionStorage.getItem("loginId");

  //os botões começam no 0
  nviagem = 0;

  $("#itemList").empty();
  //Percorrer todas as viagens, aplicando os filtros
  listaViagens.forEach(function (viagem) {
    if (
      (origem == "" || origem == viagem.origem.place) &&
      (destino == "" || destino == viagem.destino.place) &&
      (data == "" || data == viagem.data.split(" ")[0])
    ) {
      console.log(origem, destino == "", destino == viagem.destino.place, data);
      const filtrosViagens = `ID: ${viagem.v_id}<br>
                            Condutor: ${viagem.condutor}<br>
                            Origem: ${viagem.origem.place}<br>
                            Destino: ${viagem.destino.place}<br>
                            Data: ${viagem.data}<br>
                            Passageiros: ${viagem.passageiros.join(", ")}<br>`;

      $("#itemList").append("<li>" + filtrosViagens);
      //guardar na variavel join o id da viagem, para aplicar a operação correspondente
      viagemjoinLeave = viagem.v_id;

      //Verificar se o passageiro esta na viagem e adicionar os botões caso esteja ou não
      if (viagem.passageiros.includes(passageiro)) {
        $("#itemList").append(
          '<button id="botaoDeixar' +
            nviagem +
            '" type="button" onclick="ServerOperacoes(' +
            nviagem +
            ',11)">Deixar viagem</button>'
        );
        $("#itemList").append(
          '<button id="botaoJuntar' +
            nviagem +
            '" style="display: none;" type="button" onclick="ServerOperacoes(' +
            nviagem +
            ',10)">Juntar-se</button>'
        );
        $("#itemList").append("<br><br></li>");
      } else {
        $("#itemList").append(
          '<button id="botaoJuntar' +
            nviagem +
            '"  type="button" onclick="ServerOperacoes(' +
            nviagem +
            ',10)">Juntar-se</button>'
        );
        $("#itemList").append(
          '<button id="botaoDeixar' +
            nviagem +
            '" style="display: none;" type="button" onclick="ServerOperacoes(' +
            nviagem +
            ',11)">Deixar viagem</button>'
        );
        $("#itemList").append("<br><br></li>");
      }
      //Incremento para o próximo botão
      nviagem++;
    }
  });
  Reset();
}

