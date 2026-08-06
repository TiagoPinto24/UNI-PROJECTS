package TW.Controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import TW.Enteties.Pedido;
import TW.Services.PedidoService;

@RestController
@RequestMapping("/pedido")
@CrossOrigin(origins = "file://")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }
    
    @PostMapping("/add")
    public Pedido addPedido(@RequestBody Pedido pedido) {
        return pedidoService.addPedido(pedido);
    }


    @DeleteMapping("/deleteall")
    public void deleteAll() {
        pedidoService.deleteAll();
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deletebyID(@PathVariable int id, @RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        if (pedidoService.deletebyID(id, username))
            return true;
        return false;
    }

    @GetMapping("/{id}")
    public Optional<Pedido> getPedidobyID(@PathVariable int id) {
        return pedidoService.getPedidobyID(id);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Pedido>> findAll() {
        return ResponseEntity.ok(pedidoService.findAll());
    }
}
