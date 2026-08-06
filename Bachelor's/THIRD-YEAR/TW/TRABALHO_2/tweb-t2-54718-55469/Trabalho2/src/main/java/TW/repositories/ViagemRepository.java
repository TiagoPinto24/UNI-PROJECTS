package TW.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import TW.Enteties.Viagem;

public interface ViagemRepository extends JpaRepository<Viagem, Integer> {
}