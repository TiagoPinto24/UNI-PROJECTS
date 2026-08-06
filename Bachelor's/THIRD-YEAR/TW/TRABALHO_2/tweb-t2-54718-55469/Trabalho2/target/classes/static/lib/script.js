//variavel para ajudar na execução das operações o join e o leave e no change de status
let carry = 0;
//variavel usada para nomear os botões de Join e Leave para cada viagem e poder alterá-los individualmente
let nbotao = 0;
//variavel usada para guardar os dados do search vindos do servidor
//usaremos essa variavel para posteriormente podermos aplicar filtros localmente, sem chamar o servidor novamente
let listaViagens;

//Função para validar a entrada e guardar o ID no sessionStorage
function validarEntrada(tipo) {
  //tipo 1 é no caso de condutor
  if (tipo == 1) {
    var loginId = document.getElementById("IDCondutor").value;
    var password = document.getElementById("password").value;
    if (!loginId || !password) {
      document.getElementById("IDCondutor").style.border = "2px solid red";
      document.getElementById("password").style.border = "2px solid red";
      return;
    }
    $.ajax({
      url: "http://localhost:8080/usuario/validate/" + loginId + "/" + password,
      method: "GET",
      success: function (response) {
        console.log(response);
        if(response){
        sessionStorage.setItem("loginId", loginId);
        window.location.href = "Condutor.html";
        }
        else
          alert("Login ou password errados");
      },
      error: function (xhr, status, error) {
        alert("Erro ao registar condutor: " + error);
        console.error(xhr, status, error);
      },
    })
  }
  //tipo 2 é no caso de passageiro
  if (tipo == 2) {
    var loginId = document.getElementById("IDPassageiro").value;
    var password = document.getElementById("password").value;
    if (!loginId || !password) {
      document.getElementById("IDPassageiro").style.border = "2px solid red";
      document.getElementById("password").style.border = "2px solid red";
      return;
    }
    $.ajax({
      url: "http://localhost:8080/usuario/validate/" + loginId + "/" + password,
      method: "GET",
      success: function (response) {
        console.log(response);
        if(response){
        sessionStorage.setItem("loginId", loginId);
        window.location.href = "Passageiro.html";
        }
        else
          alert("Login ou password errados");
      },
      error: function (xhr, status, error) {
        alert("Erro ao registar condutor: " + error);
        console.error(xhr, status, error);
      },
    })
  }
  //tipo 3 é no caso de admin
  if (tipo == 3) {
    var loginId = document.getElementById("IDAdmin").value;
    var password = document.getElementById("password").value;
    if (!loginId || !password) {
      document.getElementById("IDAdmin").style.border = "2px solid red";
      document.getElementById("password").style.border = "2px solid red";
      return;
    }
    $.ajax({
      url: "http://localhost:8080/usuario/validate/" + loginId + "/" + password,
      method: "GET",
      success: function (response) {
        console.log(response);
        if (response) {
          sessionStorage.setItem("loginId", loginId);
          window.location.href = "Admin.html";
        } else 
          alert("Login ou password errados");
      },
      error: function (xhr, status, error) {
        alert("Erro ao registar condutor: " + error);
        console.error(xhr, status, error);
      },
    });
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
function ServerOperacoes(aux1, aux2, tipo) {
  const ADICIONA_PEDIDO = "http://localhost:8080/pedido/add";
  const ADICIONA_VIAGEM = "http://localhost:8080/viagem/add";
  const LISTA_VIAGENS = "http://localhost:8080/viagem/all";
  const LISTA_PEDIDOS = "http://localhost:8080/pedido/all";
  const REMOVE_PEDIDO = "http://localhost:8080/pedido/delete";
  const REMOVE_VIAGEM = "http://localhost:8080/viagem/delete";
  const GET_CONDUTOR = "http://localhost:8080/usuario/condutor";
  const GET_PASSAGEIRO = "http://localhost:8080/usuario/passageiro";
  const PROCURA_VIAGEM = "http://localhost:8080/viagem/search";
  const ASSOCIAR_VIAGEM = "http://localhost:8080/viagem/join";
  const CANCELAR_VIAGEM = "http://localhost:8080/viagem/leave";
  const NOVO_USUARIO = "http://localhost:8080/usuario/add";
  const LISTA_USUARIOS = "http://localhost:8080/usuario/all";
  const ALTERAR_STATUS = "http://localhost:8080/usuario/change";

  switch (tipo) {
    //Adicionar uma nova viagem
    case 1:
      var condutor = sessionStorage.getItem("loginId");
      var localPartida = $("#localPartidaCond").val();
      var localChegada = $("#LocalChegadaCond").val();
      var data = $("#DataCond").val();

      // Verificar se todos os campos foram preenchidos
      if (localPartida == "" || localChegada == "" || data == "") {
        $("#localPartidaCond").style.border = "2px solid red";
        $("#LocalChegadaCond").style.border = "2px solid red";
        $("#DataCond").style.border = "2px solid red";
        return;
      }

      $.ajax({
          url: ADICIONA_VIAGEM, // The endpoint URL
          method: "POST", // HTTP method
          contentType: "application/json",
          data: JSON.stringify({
              idCondutor: condutor,
              origem: localPartida,
              destino: localChegada,
              data: data
          }),
          success: function (response) {
              console.log(response);
              Reset();
          },
          error: function (xhr, status, error) {
              alert("Erro ao registar viagem: " + error);
              console.error(xhr, status, error);
          }
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
        $("#localPartidaPass").style.border = "2px solid red";
        $("#localChegadaPass").style.border = "2px solid red";
        $("#DataPass").style.border = "2px solid red";
        return;
      }

      $.ajax({
        url: ADICIONA_PEDIDO,
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
          passageiro: passageiro,
          origem: localPartida,
          destino: localChegada,
          data: data,
        }),
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
          console.log(response);
          if (response.length > 0 && aux1 != 1) {
            $("#itemList").empty();
            response.forEach(viagem => {
                const adicionaViagem = `<strong>ID:</strong> ${viagem.id}<br><strong>Origem:</strong> ${viagem.origem}<br><strong>Destino:</strong> ${viagem.destino}<br><strong>Data:</strong> ${viagem.data}
                <br><strong>Passageiros:</strong> ${viagem.passageiros.length}<br><br>`;
                $("#itemList").append("<li>" + adicionaViagem + "</li>");
            });
          }
          else if (response.length > 0 && aux1 == 1){
            console.log(response);
            listaViagens = response;
          }
          else if (response.length == 0)
            $("#itemList").append("<li>Lista vazia.</li>"); 
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
          console.log(response);
          $("#itemList").empty();
          response.forEach(pedido => {
              const adicionaPedido = `<strong>ID:</strong> ${pedido.id}<br><strong>Origem:</strong> ${pedido.origem}<br><strong>Destino:</strong> ${pedido.destino}<br><strong>Data:</strong> ${pedido.data}<br><br>`;
              $("#itemList").append("<li>" + adicionaPedido + "</li>");
          });
        },
        error: function (xhr, status, error) {
          console.error("Erro em aceder á lista", status, error);
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
        $("#IDViagem").style.border = "2px solid red";
        return;
      }

      $.ajax({
        url: REMOVE_VIAGEM + "/" + viagemID,
        method: "DELETE",
        contentType: "application/json",
        data: JSON.stringify({
          username: condutor,
        }),
        success: function (response) {
          console.log(response);
          if (!response)
            alert("Erro ao remover viagem");
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
        $("#IDViagem").style.border = "2px solid red";
        return;
      }

      $.ajax({
        url: REMOVE_PEDIDO + "/" + pedidoID,
        method: "DELETE",
        contentType: "application/json",
        data: JSON.stringify({
          username: passageiro,
        }),
        success: function (response) {
          console.log(response);
          if (!response)
            alert("Erro ao remover pedido");
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
        $("#IDToCondutor").style.border = "2px solid red";
        return;
      }

      $.ajax({
        url: GET_PASSAGEIRO + "/" + toCondutor,
        method: "GET",
        success: function (response) {
          console.log(response);
          $("#itemList").empty();
          if (response) {
            const fiabilidade = `<strong>Pontuação de Fiabilidade:</strong> ${response.fiabilidade}<br><br>`;
            $("#itemList").append("<li>" + fiabilidade + "</li>");
            const ultimasViagens = response.viagens.slice(-10);
            ultimasViagens.forEach(ultimasViagens => {
              const adicionaPedido = `ID: ${ultimasViagens.id}<br>Origem: ${ultimasViagens.origem}<br>Destino: ${ultimasViagens.destino}<br>Data: ${ultimasViagens.data}<br><br>`;
              $("#itemList").append("<li>" + adicionaPedido + "</li>");
            });
          }
        },
        error: function (xhr, status, error) {
          console.error("Erro em aceder á lista", status, error);
          $("#itemList").append("<li>Erro.</li>");
        },
      });
      break;

    //Informação sobre os condutores, para os passageiros
    case 8:
      //ID do passageiro que se quer informação
      var toPassageiro = $("#IDToPassageiro").val();

      //Verificar se o campo foi preenchido
      if (toPassageiro == "") {
        $("#IDToPassageiro").style.border = "2px solid red";
        return;
      }

      $.ajax({
        url: GET_CONDUTOR + "/" + toPassageiro,
        method: "GET",
        success: function (response) {
          $("#itemList").empty();
          if (response) {
            const fiabilidade = `<strong>Pontuação de Fiabilidade:</strong> ${response.fiabilidade}<br><br>`;
            $("#itemList").append("<li>" + fiabilidade + "</li>");
            const ultimasViagens = response.viagens.slice(0, 10);
            ultimasViagens.forEach(ultimasViagens => {
              const adicionaPedido = `ID: ${ultimasViagens.id}<br>Origem: ${ultimasViagens.origem}<br>Destino: ${ultimasViagens.destino}<br>Data: ${ultimasViagens.data}<br><br>`;
              $("#itemList").append("<li>" + adicionaPedido + "</li>");
            });
          }
        },
        error: function (xhr, status, error) {
          console.error("Erro em aceder á lista", status, error);
          $("#itemList").append("<li>Erro.</li>");
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
      nbotao = 0;
      carry = 0;

      //data, pelo menos um dos campos tem de estar preenchido
      let URL = PROCURA_VIAGEM + "/";
      if (origemFiltros) URL = URL + "origem:" + origemFiltros + "_";
      if (destinoFiltros) URL = URL + "destino:" + destinoFiltros + "_";
      if (dataFiltros) URL = URL + "data:" + dataFiltros;
      if (origemFiltros == "" && destinoFiltros == "" && dataFiltros == "") {
        $("#OrigemToFiltros").style.border = "2px solid red";
        return;
      }

      $.ajax({
        url: URL,
        type: "GET",
        success: function (response) {
          if (response) {
            console.log(response);
            $("#itemList").empty();
            //Percorrer as viagens resultantes da chamada do server
            response.forEach(viagem => {
              const adicionaViagem = `<strong>ID:</strong> ${viagem.id}<br><strong>Origem:</strong> ${viagem.origem}<br><strong>Destino:</strong> ${viagem.destino}<br><strong>Data:</strong> ${viagem.data}
              <br><strong>Passageiros:</strong> ${viagem.passageiros.length}<br><br>`;
              $("#itemList").append("<li>" + adicionaViagem + "</li>");

              //guardar na variavel join o id da viagem, para aplicar a operação correspondente
              carry = viagem.id;

              var isUsernamePresent = viagem.passageiros.some(function(p) {
                return p.username === passageiro;
            });
              //Verificar se o passageiro esta na viagem e adicionar os botões caso esteja ou não
                if (isUsernamePresent) {
                  $("#itemList").append(
                    '<button id="botaoDeixar' +
                      nbotao + 
                      '" type="button" onclick="ServerOperacoes(' +
                      nbotao + ',' + carry +
                      ',11)">Deixar viagem</button>'
                  );
                  $("#itemList").append(
                    '<button id="botaoJuntar' +
                      nbotao +
                      '" style="display: none" type="button" onclick="ServerOperacoes(' +
                      nbotao + ',' + carry +
                      ',10)">Juntar-se</button>'
                  );
                  $("#itemList").append("<br><br></li>");
                } else {
                  $("#itemList").append(
                    '<button id="botaoJuntar' +
                      nbotao +
                      '" type="button" onclick="ServerOperacoes(' +
                      nbotao + ',' + carry +
                      ',10)">Juntar-se</button>'
                  );
                  $("#itemList").append(
                    '<button id="botaoDeixar' +
                      nbotao +
                      '" style="display: none" type="button" onclick="ServerOperacoes(' +
                      nbotao + ',' + carry +
                      ',11)">Deixar viagem</button>'
                  );
                  $("#itemList").append("<br><br></li>");
                }
                //incremento para o proximo botão
                nbotao++;
                
            });
            nbotao = 0;
            ServerOperacoes(1,0,3);
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
      var IDViagem = aux2;

      $.ajax({
        url: ASSOCIAR_VIAGEM + "/" + IDViagem,
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
          username: passageiro,
        }),
        success: function (response) {
          //trocar os botões de Juntar-se para Deixar viagem
          document.getElementById("botaoJuntar" + aux1).style.display = "none";
          document.getElementById("botaoDeixar" + aux1).style.display = "";
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
      var IDViagem = aux2;

      $.ajax({
        url: CANCELAR_VIAGEM + "/" + IDViagem,
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
          username: passageiro,
        }),
        success: function (response) {
          //trocar os botões de Juntar-se para Deixar viagem
          document.getElementById("botaoJuntar" + aux1).style.display = "";
          document.getElementById("botaoDeixar" + aux1).style.display = "none";
          console.log(response);
        },
        error: function (response) {
          alert("Erro ao associar-se à viagem.");
          console.log(response);
        },
      });
      break;

    //Criar Conta
    case 12:
      var username = $("#IDUsuario").val();
      var password = $("#password").val();

      if (username == "" || password == "") {
        $("#IDUsuario").style.border = "2px solid red";
        $("#password").style.border = "2px solid red";
        return;
      }
      let requestdata = {};
      requestdata.username = username;
      requestdata.password = password;
      requestdata.fiabilidade = 5;
      if (aux1 == 10) requestdata.papel = "passageiro";
      else requestdata.papel = "condutor";
      requestdata.ativa = false;

      $.ajax({
        url: NOVO_USUARIO,
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(requestdata),
        success: function (response) {
          alert("Conta criada com sucesso!");
          console.log(response);
          Reset();
        },
        error: function (response) {
          alert("Erro ao criar conta.");
          console.log(response);
        },
      });
      break; 
      
      //Listar Usuários para posteriormente alterar os satus, apenas aplicável ao administrador
      case 13:
        $.ajax({
          url: LISTA_USUARIOS,
          type: "GET",
          success: function (response) {
            console.log(response);
            $("#itemList").empty();
      
            if (response.length > 0) {
              response.forEach((usuario, index) => {
                carry = usuario.username;
                const adicionauser = `
                  <strong>Nome:</strong> ${usuario.username}<br>
                  <strong>Papel:</strong> ${usuario.papel}<br>
                  <strong>Atividade:</strong> ${usuario.ativa ? "Ativo" : "Inativo"}<br>
                  <strong>Fiabilidade:</strong> ${usuario.fiabilidade}<br>
                `;
                $("#itemList").append("<li>" + adicionauser + "</li>");
                
                const botao = '<button id="alterarStatus" type="button" onclick="ServerOperacoes(0,\'' + carry + '\', 14)">Alterar status</button>';
                $("#itemList").append(botao);
                              
              });
            } else {
              $("#itemList").append("<li>Não há usuários registrados.</li>");
            }
          },
          error: function (xhr, status, error) {
            console.error("Erro ao listar os usuários", status, error);
            $("#itemList").append("<li>Erro ao carregar a lista de usuários.</li>");
          },
        });
        break;

        //Alterar o status de um usuário
        case 14:
          var username = aux2;

          var url = ALTERAR_STATUS + "/" + username;
          $.ajax({
            url: ALTERAR_STATUS,
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({
              username: username,
            }),
            success: function (response) {
              console.log(response);
            },
            error: function (response) {
              alert("Erro ao associar-se à viagem.");
              console.log(response);
            }
          })
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
  nbotao = 0;

  $("#itemList").empty();
  //Percorrer todas as viagens, aplicando os filtros
  listaViagens.forEach(viagem => {
    if (
      (origem == "" || origem == viagem.origem) &&
      (destino == "" || destino == viagem.destino) &&
      (data == "" || data == viagem.data)
    ) {
      const adicionaViagem = `<strong>ID:</strong> ${viagem.id}<br><strong>Origem:</strong> ${viagem.origem}<br><strong>Destino:</strong> ${viagem.destino}<br><strong>Data:</strong> ${viagem.data}
      <br><strong>Passageiros:</strong> ${viagem.passageiros.length}<br><br>`;
              $("#itemList").append("<li>" + adicionaViagem + "</li>");

              carry = viagem.id;

              var isUsernamePresent = viagem.passageiros.some(function(p) {
                return p.username===passageiro;
            });
              //Verificar se o passageiro esta na viagem e adicionar os botões caso esteja ou não
                if (isUsernamePresent) {
                  $("#itemList").append(
                    '<button id="botaoDeixar' +
                      nbotao +
                      '" type="button" onclick="ServerOperacoes(' +
                      nbotao + ',' + carry +
                      ',11)">Deixar viagem</button>'
                  );
                  $("#itemList").append(
                    '<button id="botaoJuntar' +
                      nbotao +
                      '" style="display: none" type="button" onclick="ServerOperacoes(' +
                      nbotao + ',' + carry +
                      ',10)">Juntar-se</button>'
                  );
                  $("#itemList").append("<br><br></li>");
                } else {
                  $("#itemList").append(
                    '<button id="botaoJuntar' +
                      nbotao +
                      '" type="button" onclick="ServerOperacoes(' +
                      nbotao + ',' + carry +
                      ',10)">Juntar-se</button>'
                  );
                  $("#itemList").append(
                    '<button id="botaoDeixar' +
                      nbotao +
                      '" style="display: none" type="button" onclick="ServerOperacoes(' +
                      nbotao + ',' + carry +
                      ',11)">Deixar viagem</button>'
                  );
                  $("#itemList").append("<br><br></li>");
                }        
    }
    //incremento para o proximo botão  
    nbotao++;
  });
}

  
