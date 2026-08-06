package com.mds.projeto2;

import java.util.Date;

public class manutencao {

    String tipo;
    Boolean realizada;
    Date dataRegisada;
    Date dataConcluida;

    public manutencao(String tipo) {
        this.tipo = tipo;
        this.realizada = false;
        this.dataRegisada = new Date(System.currentTimeMillis());
    }

    public void concluir() {
        this.realizada = true;
        this.dataConcluida = new Date(System.currentTimeMillis());
    }

    public String getTipo() {
        return tipo;
    }

    public Boolean getRealizada() {
        return realizada;
    }

    public Date getDataRegisada() {
        return dataRegisada;
    }

    public Date getDataConcluida() {
        return dataConcluida;
    }
}