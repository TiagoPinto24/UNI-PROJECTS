const canvas = document.getElementById('newCanvas');
const ctx = canvas.getContext('2d');

canvas.width = window.innerWidth / 3;
canvas.height = window.innerHeight;

// Função para desenhar a estrada 
function drawRoad() {
    // Cor do piso
    ctx.fillStyle = '#333';
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    // Cor da faixa 
    ctx.fillStyle = '#fff';

    // Largura da faixa e espaço
    const faixaWidth = 80;
    const faixasSpacing = 40;

    // Número de faixas
    const numfaixas = Math.floor(canvas.height / (faixaWidth + faixasSpacing));

    // Desenha as faixas na estrada
    for (let i = 0; i < numfaixas; i++) {
        const x = canvas.width / 2 - faixaWidth / 2;
        const y = i * (faixaWidth + faixasSpacing) + faixasSpacing / 2;
        ctx.fillRect(x, y, faixaWidth, faixaWidth);
    }
}

// Função para desenhar o carro 
function drawCar(x, y) {
    const tamanho = 8;

    const offsetX = x;
    const offsetY = y;

    ctx.save(); // Salva o estado atual do contexto
    ctx.translate(offsetX, offsetY);
    ctx.scale(tamanho, tamanho);

    // Rodas da frente
    ctx.fillStyle = "#321b41";

    // Primeira roda
    ctx.beginPath();
    ctx.moveTo(6, 29);
    ctx.lineTo(8, 29);
    ctx.quadraticCurveTo(9, 29, 9, 28);
    ctx.lineTo(9, 24);
    ctx.lineTo(5, 24);
    ctx.lineTo(5, 28);
    ctx.quadraticCurveTo(5, 29, 6, 29);
    ctx.closePath();
    ctx.fill();

    // Segunda roda
    ctx.beginPath();
    ctx.moveTo(24, 29);
    ctx.lineTo(26, 29);
    ctx.quadraticCurveTo(27, 29, 27, 28);
    ctx.lineTo(27, 24);
    ctx.lineTo(23, 24);
    ctx.lineTo(23, 28);
    ctx.quadraticCurveTo(23, 29, 24, 29);
    ctx.closePath();
    ctx.fill();

    // Rodas de trás
    ctx.fillStyle = "#212121";
    ctx.beginPath();
    ctx.moveTo(10, 19);
    ctx.lineTo(7, 19);
    ctx.bezierCurveTo(6, 19, 6, 18, 6, 18);
    ctx.lineTo(6, 17);
    ctx.bezierCurveTo(6, 16, 6, 16, 7, 16);
    ctx.lineTo(8, 16);
    ctx.bezierCurveTo(9, 16, 10, 17, 10, 19);
    ctx.closePath();
    ctx.fill();

    ctx.fillStyle = "#212121";
    ctx.beginPath();
    ctx.moveTo(22, 19);
    ctx.lineTo(25, 19);
    ctx.bezierCurveTo(25, 19, 26, 18, 26, 18)
    ctx.lineTo(26, 17);
    ctx.bezierCurveTo(26, 16, 25, 16, 25, 16)
    ctx.lineTo(24, 16);
    ctx.bezierCurveTo(23, 16, 22, 17, 22, 19);
    ctx.bezierCurveTo(22, 19, 22, 19, 22, 19)
    ctx.closePath();
    ctx.fill();

    // Parte da frente
    ctx.fillStyle = "#9b9b9b";
    ctx.beginPath();
    ctx.moveTo(25, 26);
    ctx.lineTo(7, 26);
    ctx.bezierCurveTo(6, 26, 5, 25, 5, 24);
    ctx.lineTo(5, 22);
    ctx.lineTo(27, 22);
    ctx.lineTo(27, 24);
    ctx.bezierCurveTo(27, 25, 26, 26, 25, 26);
    ctx.closePath();
    ctx.fill();

    // Matrícula
    ctx.fillStyle = "#d3d3d3";
    ctx.beginPath();
    ctx.lineTo(17, 25);
    ctx.lineTo(15, 25);
    ctx.bezierCurveTo(14, 25, 14, 25, 14, 26);
    ctx.lineTo(14, 26);
    ctx.bezierCurveTo(14, 27, 14, 27, 15, 27);
    ctx.lineTo(17, 27);
    ctx.bezierCurveTo(18, 27, 18, 27, 18, 26);
    ctx.lineTo(18, 26);
    ctx.bezierCurveTo(18, 25, 18, 25, 17, 25);
    ctx.closePath();
    ctx.fill();

    // Espelho
    ctx.fillStyle = "#26c9fc";
    ctx.beginPath();
    ctx.moveTo(9, 12);
    ctx.lineTo(7, 18);
    ctx.lineTo(16, 19);
    ctx.lineTo(25, 18);
    ctx.lineTo(23, 12);
    ctx.closePath();
    ctx.fill();

    // Corpo do carro
    ctx.fillStyle = "#f8312f";
    ctx.beginPath();
    ctx.moveTo(12, 10);
    ctx.bezierCurveTo(11, 10, 10, 11, 9, 12);
    ctx.lineTo(23, 12);
    ctx.bezierCurveTo(22, 11, 21, 10, 19, 10);
    ctx.closePath();
    ctx.fill();
    ctx.beginPath();
    ctx.moveTo(7, 18);
    ctx.lineTo(25, 18);
    ctx.bezierCurveTo(26, 19, 27, 20, 27, 21);
    ctx.lineTo(27, 23);
    ctx.lineTo(5, 23);
    ctx.lineTo(5, 21);
    ctx.bezierCurveTo(5, 20, 6, 19, 7, 18);
    ctx.closePath();
    ctx.fill();

    // Piscas
    ctx.fillStyle = "#d3d3d3";
    ctx.fillRect(5, 20, 3, 2.5);
    ctx.fillRect(24, 20, 3, 2.5);

    // Chapa
    ctx.fillStyle = "#636363";
    ctx.beginPath();
    ctx.moveTo(18, 20);
    ctx.lineTo(17, 20);
    ctx.lineTo(18, 20);
    ctx.lineTo(14, 20);
    ctx.quadraticCurveTo(13, 20, 13, 20);
    ctx.quadraticCurveTo(13, 22, 13, 22);
    ctx.lineTo(14, 22);
    ctx.lineTo(18, 22);
    ctx.lineTo(17, 22);
    ctx.lineTo(18, 22);
    ctx.quadraticCurveTo(19, 22, 19, 22);
    ctx.quadraticCurveTo(19, 20, 19, 20);
    ctx.closePath();
    ctx.fill();

    // Faróis
    ctx.fillStyle = "#ffb02e";
    ctx.beginPath();
    ctx.arc(10.5, 21, 1.5, 0, 2 * Math.PI);
    ctx.arc(21.5, 21, 1.5, 0, 2 * Math.PI);
    ctx.fill();

    ctx.scale(1 / tamanho, 1 / tamanho);
    ctx.translate(-offsetX, -offsetY);

    ctx.restore();
}

// Função para desenhar a estrela
function drawStar(x, y, size, lados) {
    ctx.fillStyle = 'yellow';
    ctx.beginPath();
    for (let i = 0; i < lados * 2; i++) {
        const angle = (i * Math.PI) / lados;
        const radius = i % 2 === 0 ? size / 2 : size / 4;
        const starX = x + radius * Math.cos(angle);
        const starY = y + radius * Math.sin(angle);
        ctx.lineTo(starX, starY);
    }
    ctx.closePath();
    ctx.fill();
}

// Função para desenhar um quadro da animação
function drawFrame() {

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    drawRoad();

    drawCar(carParameters.x, carParameters.y);

    drawStar(starParameters.x, starParameters.y, starRadius, starPoints);
}

// Parâmetros da estrela
const starRadius = 30;
const starPoints = 5;
const starColor = 'yellow';

const carParameters = { x: canvas.width / 2 - 128, y: -80 };

const starParameters = { x: canvas.width / 2, y: -20 };

// Função para animar o carro a descer a estrada
function animateCar() {
    carParameters.y = -200;

    const carTween = new TWEEN.Tween(carParameters)
        .to({ y: canvas.height }, 4000)
        .easing(TWEEN.Easing.Linear.None)
        .onUpdate(() => {
            drawFrame();
        })
        .onComplete(() => {
            animateCar();
        });

    carTween.start();
}

// Função para animar a estrela 
function animateStar() {
    const starTween = new TWEEN.Tween(starParameters)
        .to(carParameters, 4000)
        .easing(TWEEN.Easing.Linear.None)
        .onUpdate(() => {
            drawFrame();
        })
        .onComplete(() => {
            animateStar();
        });

    starTween.start();
}

drawRoad();

const carX = canvas.width / 2 - 100 / 2;

animateCar();

animateStar();

// Função para atualizar a animação
function animate() {
    requestAnimationFrame(animate);
    TWEEN.update();
}

animate(); 