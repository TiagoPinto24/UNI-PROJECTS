#!/bin/bash

# Iniciar o Server em segundo plano
echo "Iniciando o Server..."
cd /home/tiago/sd-t02-54718-55469  # para correr o shell script é necessário alterar este caminho, de forma a ser compativel com o seu dispositvo
/usr/bin/env /usr/lib/jvm/java-11-openjdk-amd64/bin/java @/tmp/cp_3z958h4q8vw2b50dsvo4lpfim.argfile SD.Server > outputs/server_output.log 2>&1 &
server_pid=$!
echo "Server iniciado com PID: $server_pid"

# Iniciar o IoTDevicePublisher em segundo plano
echo "Iniciando o IoTDevicePublisher..."
/usr/bin/env /usr/lib/jvm/java-11-openjdk-amd64/bin/java @/tmp/cp_3z958h4q8vw2b50dsvo4lpfim.argfile SD.IoTDevicePublisher > outputs/publisher_output.log 2>&1 &
publisher_pid=$!
echo "IoTDevicePublisher iniciado com PID: $publisher_pid"

# Iniciar o Client_admin em primeiro plano
echo "Iniciando o Client_admin..."
/usr/bin/env /usr/lib/jvm/java-11-openjdk-amd64/bin/java @/tmp/cp_3z958h4q8vw2b50dsvo4lpfim.argfile SD.Client_admin

# Matar o que está em segundo plano
kill $server_pid
kill $publisher_pid

# Apagar os ficheiros de output após a execução
rm -f outputs/server_output.log outputs/publisher_output.log
