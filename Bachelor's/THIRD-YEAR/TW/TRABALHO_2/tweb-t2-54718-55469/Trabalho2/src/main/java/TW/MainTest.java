package TW;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import TW.Enteties.Usuario;
import TW.Enteties.Viagem;
import TW.Services.PedidoService;
import TW.Services.UsuarioService;
import TW.Services.ViagemService;

import java.sql.Date;

@SpringBootApplication
public class MainTest {
    public static void main(String[] args) {
		SpringApplication.run(MainTest.class, args);
	}
	@Bean
    public CommandLineRunner test(UsuarioService usuarioService, ViagemService viagemService, PedidoService pedidoService) {
    return (args) -> {
        viagemService.deleteAll();
        usuarioService.deleteAll();
        pedidoService.deleteAll();

        usuarioService.addUsuario(new Usuario("admin", "admin", "administrador",5.0f, true));

        usuarioService.addUsuario(new Usuario("joao", "joao", "passageiro", 4.7f, true));
        usuarioService.addUsuario(new Usuario("tiago", "tiago", "passageiro",4.5f, true));
        usuarioService.addUsuario(new Usuario("francisco", "francisco", "passageiro",3.2f, true));
        usuarioService.addUsuario(new Usuario("maria", "maria", "passageiro",4.0f, true));
        usuarioService.addUsuario(new Usuario("duarte", "duarte", "passageiro",2.7f, true));

        usuarioService.addUsuario(new Usuario("mauro", "mauro", "condutor",4.5f, true));
        usuarioService.addUsuario(new Usuario("gonçalo", "gonçalo", "condutor",5.0f, true));
        usuarioService.addUsuario(new Usuario("tomas", "tomas", "condutor",1.6f, true));
        
        Viagem trip = new Viagem("mauro", "Verney", "Giraldo", Date.valueOf("2025-01-25"));
        Viagem trip3 = new Viagem("mauro", "CPF", "Verney", Date.valueOf("2025-01-23"));
        Viagem trip4 = new Viagem("mauro", "Mitra", "CES", Date.valueOf("2025-01-22"));
        Viagem trip5 = new Viagem("mauro", "CES", "Mitra", Date.valueOf("2025-01-21"));

        viagemService.addViagem(trip);
        viagemService.addViagem(trip3);
        viagemService.addViagem(trip4);
        viagemService.addViagem(trip5);

        viagemService.joinViagem(trip.getId(), usuarioService.getUsuarioByUsername("francisco").get());
        viagemService.joinViagem(trip.getId(), usuarioService.getUsuarioByUsername("joao").get());
        viagemService.joinViagem(trip.getId(), usuarioService.getUsuarioByUsername("tiago").get());
        viagemService.joinViagem(trip.getId(), usuarioService.getUsuarioByUsername("maria").get());

        viagemService.joinViagem(trip3.getId(), usuarioService.getUsuarioByUsername("joao").get());
        viagemService.joinViagem(trip3.getId(), usuarioService.getUsuarioByUsername("maria").get());
        viagemService.joinViagem(trip3.getId(), usuarioService.getUsuarioByUsername("tiago").get());

        viagemService.joinViagem(trip4.getId(), usuarioService.getUsuarioByUsername("francisco").get());
        viagemService.joinViagem(trip4.getId(), usuarioService.getUsuarioByUsername("duarte").get());

        viagemService.joinViagem(trip5.getId(), usuarioService.getUsuarioByUsername("francisco").get());

        viagemService.addViagem(trip);
        viagemService.addViagem(trip3);
        viagemService.addViagem(trip4);
        viagemService.addViagem(trip5);
    };
}
}