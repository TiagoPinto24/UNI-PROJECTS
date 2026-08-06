package com.mds.projeto2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class testeRemoverQuarto {
    @Test
    public void testRemoverQuarto() {
        ArrayList<quarto> quartos = new ArrayList<>();
        Main.adicionarQuarto(quartos, 1, 2, 2, true, true, "Piscina");

        Main.removerQuarto(quartos, 0);

        assertEquals(0, quartos.size(), "o numero de quartos deve ser 0");
    }
}
