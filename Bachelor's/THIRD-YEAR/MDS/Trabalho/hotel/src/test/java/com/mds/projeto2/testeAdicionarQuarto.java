package com.mds.projeto2;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class testeAdicionarQuarto {
    @Test
    public void testAdicionarQuarto() {
        ArrayList<quarto> quartos = new ArrayList<>();

        Main.adicionarQuarto(quartos, 1, 2, 2, true, true, "Piscina");

        assertEquals(1, quartos.get(0).getNumero(), "o numero do quarto deve ser 1");
        assertEquals(2, quartos.get(0).getCapacidadeMax(), "a capacidade maxima do quarto deve ser 2");
        assertEquals(2, quartos.get(0).getNrcamas(), "o numero de camas do quarto deve ser 2");
        assertEquals(true, quartos.get(0).getCozinha(), "o quarto deve ter cozinha");
        assertEquals(true, quartos.get(0).getVaranda(), "o quarto deve ter varanda");
        assertEquals("Piscina", quartos.get(0).getVista(), "a vista do quarto deve ser Piscina");
    }
}
