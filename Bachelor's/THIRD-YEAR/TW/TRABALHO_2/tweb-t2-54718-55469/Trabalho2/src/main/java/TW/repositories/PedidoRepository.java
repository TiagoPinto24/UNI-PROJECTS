package TW.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import TW.Enteties.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
}