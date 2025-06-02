package com.mds.projeto2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class testeRegistarManutencao {

    @Test
    public void testRegistarManutencao() {
        quarto q = new quarto(1, 2, 2, true, true, "Piscina");
        q.registarManutencao("Limpeza");
        q.registarManutencao("Iluminacao");
        assertEquals(2, q.manutencoesporConcluir(), "o numero de manutenções por realizar deve ser 2");
        q.concluirManutencao(1);
        assertEquals(1, q.manutencoesporConcluir(), "o numero de manutenções por realizar deve ser 1");
        q.concluirManutencao(2);
        assertEquals(0, q.manutencoesporConcluir(), "o numero de manutenções por realizar deve ser 0");
    }
    }

