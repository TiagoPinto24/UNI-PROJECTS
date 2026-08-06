package TW.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TW.Enteties.Pedido;
import TW.repositories.PedidoRepository;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository PedidoRepository;

    public Pedido addPedido(Pedido Pedido) {
        return PedidoRepository.save(Pedido);
    }

    public Boolean deletebyID(int id, String username) {
        if (PedidoRepository.findById(id).get().getPassageiro().equals(username)){
            PedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void deleteAll() {
        PedidoRepository.deleteAll();
    }
    
    public List<Pedido> findAll() {
        return (List<Pedido>) PedidoRepository.findAll();
    }

    public Optional<Pedido> getPedidobyID(int id) {
        return PedidoRepository.findById(id);
    }
}