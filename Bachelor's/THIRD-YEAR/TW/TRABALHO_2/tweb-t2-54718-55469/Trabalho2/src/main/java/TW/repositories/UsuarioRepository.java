package TW.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import TW.Enteties.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario,String> {
    List<Usuario> getUsusariosByPapel(String papel);
}