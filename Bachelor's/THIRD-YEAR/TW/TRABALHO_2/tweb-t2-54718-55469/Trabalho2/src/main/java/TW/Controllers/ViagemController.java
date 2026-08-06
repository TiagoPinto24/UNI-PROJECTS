package TW.Controllers;

import java.sql.Date;
import java.util.ArrayList;
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

import TW.Enteties.Viagem;
import TW.Services.UsuarioService;
import TW.Services.ViagemService;

@RestController
@RequestMapping("/viagem")
@CrossOrigin(origins = "file://")
public class ViagemController {
    @Autowired
    private ViagemService ViagemService;

    public ViagemController(ViagemService viagemService) {
        this.ViagemService = viagemService;
    }

    @PostMapping("/add")
    public Viagem addViagem(@RequestBody Map<String, String> payload) {
        Viagem viagem = new Viagem(payload.get("username"), payload.get("origem"), payload.get("destino"), Date.valueOf(payload.get("data")));
        return ViagemService.addViagem(viagem);
    }

    @DeleteMapping("/deleteall")
    public void deleteAll() {
        ViagemService.deleteAll();
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deletebyID(@PathVariable int id, @RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        if (ViagemService.deletebyID(id, username))
            return true;
        return false;
    }

    @GetMapping("/{iusername}")
    public Optional<Viagem> getViagembyID(@PathVariable int username) {
        return ViagemService.getViagembyID(username);
    }

    @GetMapping("/search/{filtros}")
    public ResponseEntity<List<Viagem>> getViagensbyOrigem(@PathVariable String filtros) {
        String[] separados = filtros.split("_");
        List<Viagem> viagens = ViagemService.getAll();
        List<Viagem> viagensFiltradas = new ArrayList<Viagem>();
        for (Viagem viagem : viagens) {
            if ((!separados[0].split(":")[0].equals("origem") || viagem.getOrigem().equals(separados[0].split(":")[1])) && 
            (!(separados.length > 1) || (!separados[1].split(":")[0].equals("destino") || viagem.getDestino().equals(separados[1].split(":")[1]))) &&
            (!(separados.length > 2) || (!separados[2].split(":")[0].equals("data") || viagem.getData().equals(Date.valueOf(separados[2].split(":")[1]))))) {
            viagensFiltradas.add(viagem);
            }
        }
        return ResponseEntity.ok(viagensFiltradas);
    }

    @GetMapping(value = "/all")
    public List<Viagem> getAll() {
        return ViagemService.getAll();
    }

    @Autowired
    UsuarioService usuarioService;
    @PostMapping("/join/{id}")
    public void joinViagem(@PathVariable int id, @RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        ViagemService.joinViagem(id, usuarioService.getUsuarioByUsername(username).get());
    }

    @PostMapping("/leave/{id}")
    public void leaveViagem(@PathVariable int id, @RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        ViagemService.leaveViagem(id, usuarioService.getUsuarioByUsername(username).get());
    }
}
