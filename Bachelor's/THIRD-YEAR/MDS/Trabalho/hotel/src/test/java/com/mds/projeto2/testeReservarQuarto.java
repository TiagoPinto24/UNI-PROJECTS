package com.mds.projeto2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class testeReservarQuarto {
    @Test
    public void testReservarQuarto() {
        ArrayList<quarto> quartos = new ArrayList<>();
        Main.adicionarQuarto(quartos, 1, 2, 2, true, true, "Piscina");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            assertEquals(false, quartos.get(0).isOcupado(dateFormat.parse("2025-01-23"), dateFormat.parse("2025-01-25")), "Deve indicar que o quarto não está ocupado nessas datas");
            quartos.get(0).setreservas(new reservas(dateFormat.parse("2025-01-23"), dateFormat.parse("2025-01-30")));
            
                assertEquals(true, quartos.get(0).isOcupado(dateFormat.parse("2025-01-20"), dateFormat.parse("2025-01-25")), "Deve indicar que o quarto esta ocupado nessa data");
                assertEquals(true, quartos.get(0).isOcupado(dateFormat.parse("2025-01-26"), dateFormat.parse("2025-02-2")), "Deve indicar que o quarto esta ocupado nessa data");
                assertEquals(false, quartos.get(0).isOcupado(dateFormat.parse("2025-01-15"), dateFormat.parse("2025-01-20")), "Deve indicar que o quarto não está ocupado nessas datas");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}