package com.mds.projeto2;

import java.util.Date;
public class reservas {
    Date dataEntrada;
    Date dataSaida;
    
    public reservas(Date dataEntrada, Date dataSaida) {
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }   

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    } 
}
