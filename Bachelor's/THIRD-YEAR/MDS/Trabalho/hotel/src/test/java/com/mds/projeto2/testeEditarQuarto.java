package com.mds.projeto2;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class testeEditarQuarto {
    @Test
    public void testEditarQuarto() {
        ArrayList<quarto> quartos = new ArrayList<>();
        Main.adicionarQuarto(quartos, 1, 2, 2, true, true, "Piscina");
        quarto bedroom = quartos.get(0);

        bedroom.setCapacidadeMax(3);
        assertEquals(3, bedroom.getCapacidadeMax(), "A capacidade máxima do quarto deve ser 3");
        bedroom.setNrcamas(3);
        assertEquals(3, bedroom.getNrcamas(), "o numero de camas do quarto deve ser 3");
        bedroom.setCozinha(false);
        assertEquals(false, bedroom.getCozinha(), "o quarto nao deve ter cozinha");
        bedroom.setVaranda(true);
        assertEquals(true, bedroom.getVaranda(), "o quarto deve ter varanda");
        bedroom.setVista("Mar");
        assertEquals("Mar", bedroom.getVista(), "a vista do quarto deve ser Mar");
    }
    
}
