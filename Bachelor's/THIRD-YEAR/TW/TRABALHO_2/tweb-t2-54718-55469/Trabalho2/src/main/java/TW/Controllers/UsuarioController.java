package TW.Controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import TW.Enteties.Usuario;
import TW.Services.UsuarioService;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "file://")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/add")
    public Usuario addUsuario(@RequestBody Map<String, String> payload) {   
        Usuario usuario = new Usuario(payload.get("username"), payload.get("password"), payload.get("papel"), Float.parseFloat(payload.get("fiabilidade")), true);
        return usuarioService.addUsuario(usuario);
    }

    @DeleteMapping("/delete")
    public void deleteAll() {
        usuarioService.deleteAll();
    }

    @DeleteMapping("/{id}")
    public void deletebyID(@PathVariable String username) {
        usuarioService.deleteUsuario(username);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Usuario>> findAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }
    
    @GetMapping("condutor/{username}")
    public Optional<Usuario> searchUsuarioID(@PathVariable String username) {
        List<Usuario> u =usuarioService.getUsuariosByPapel("condutor");
        for (Usuario us : u) {
            if (us.getUsername().equals(username))
                return Optional.of(us);
        }
        return Optional.empty();
    }

    @GetMapping("passageiro/{username}")
    public Optional<Usuario> searchUsuarioID2(@PathVariable String username) {
        System.out.println(username);
        List<Usuario> u =usuarioService.getUsuariosByPapel("passageiro");
        for (Usuario us : u) {
            if (us.getUsername().equals(username))
                return Optional.of(us);
        }
        return Optional.empty();
    }

    @GetMapping("/validate/{username}/{password}")
    public boolean validate(@PathVariable String username, @PathVariable String password) {
        return usuarioService.validate(username, password);
    }

    @PostMapping("/change")
    public void change(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        usuarioService.change(username);
    }
    
}
