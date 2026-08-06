package com.mds.projeto2;

import java.util.ArrayList;
import java.util.Date;

public class quarto {

    Integer numero;
    Integer capacidadeMax;
    Integer nrcamas;
    Boolean cozinha;
    Boolean varanda;
    String vista;
    ArrayList<reservas> reservas = new ArrayList<reservas>();
    ArrayList<manutencao> manutencoes = new ArrayList<manutencao>();

    public quarto(Integer numero, Integer capacidadeMax, Integer nrcamas, Boolean cozinha, Boolean varanda, String vista) {
        this.numero = numero;
        this.capacidadeMax = capacidadeMax;
        this.nrcamas = nrcamas;
        this.cozinha = cozinha;
        this.varanda = varanda;
        this.vista = vista;
    }

    public void registarManutencao(String tipo) {
        manutencoes.add(new manutencao(tipo));
    }

    public Integer manutencoesporConcluir() {
        Integer count = 0;
        for (manutencao m : manutencoes) {
            if (!m.getRealizada()) {
                count++;
            }
        }
        return count;
    }

    public void getManutencoes() {
        if (manutencoes.isEmpty() || manutencoesporConcluir() == 0) {
            System.out.println("Nenhuma manutenção por realizar");
            }
            for (manutencao m : manutencoes) {
                if (!m.getRealizada()) {
                    System.out.println("id: " + (manutencoes.indexOf(m) + 1));
                    System.out.println("tipo: " + m.getTipo());
                    System.out.println("data registada: " + m.getDataRegisada());
                }
            }
    }

    public void getTodasManutencoes() {
        if (manutencoes.isEmpty()) {
            System.out.println("Nenhuma manutenção registada");
            return;
        }
        for (manutencao m : manutencoes) {
            System.out.println("id: " + (manutencoes.indexOf(m) + 1));
            System.out.println("tipo: " + m.getTipo());
            System.out.println("data registada: " + m.getDataRegisada());
            if (m.getRealizada()) {
                System.out.println("Concluida em: " + m.getDataConcluida());
            }
        }
    }

    public void concluirManutencao(Integer id) {
        manutencao m = manutencoes.get(id - 1);
        if (m.getRealizada()) {
            System.out.println("Manutenção " + m.getTipo() + " já concluida");
            return;
        }
        m.concluir();
        System.out.println("Manutenção \"" + m.getTipo() + "\" concluída a " + m.getDataConcluida());
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getCapacidadeMax() {
        return capacidadeMax;
    }

    public void setCapacidadeMax(Integer capacidadeMax) {
        this.capacidadeMax = capacidadeMax;
    }

    public Integer getNrcamas() {
        return nrcamas;
    }

    public void setNrcamas(Integer nrcamas) {
        this.nrcamas = nrcamas;
    }

    public Boolean getCozinha() {
        return cozinha;
    }

    public void setCozinha(Boolean cozinha) {
        this.cozinha = cozinha;
    }

    public Boolean getVaranda() {
        return varanda;
    }

    public void setVaranda(Boolean varanda) {
        this.varanda = varanda;
    }

    public String getVista() {
        return vista;
    }

    public void setVista(String vista) {
        this.vista = vista;
    }

    public String toString() {
        return "Quarto " + this.numero + " - Capacidade maxima: " + this.capacidadeMax + " - Numero de camas: " + this.nrcamas + " - Cozinha: " + this.cozinha + " - Varanda: " + this.varanda + " - Vista: " + this.vista;
    }

    public void setreservas(reservas reservas) {
        if (!isOcupado(reservas.dataEntrada, reservas.dataSaida))
            this.reservas.add(reservas);
        else
            System.out.println("Quarto ocupado");  
    }

    public boolean isOcupado(Date datacomeco,Date datafim) {
        if (reservas.isEmpty())
            return false;
        for (reservas reserva : this.reservas) {
            if (datafim.after(reserva.dataEntrada) && datacomeco.before(reserva.dataSaida))
                return true;    
        }
        return false;
    }
}