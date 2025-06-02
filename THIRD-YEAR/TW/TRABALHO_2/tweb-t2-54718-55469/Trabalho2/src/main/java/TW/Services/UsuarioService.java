package TW.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TW.Enteties.Usuario;
import TW.repositories.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository UsuarioRepository;

    public Usuario addUsuario(Usuario usuario) {
        if (!UsuarioRepository.findById(usuario.getUsername()).isEmpty())
            return null;   
        return UsuarioRepository.save(usuario);
    }

    public void deleteUsuario(String username) {
        UsuarioRepository.deleteById(username);
    }
    
    public Optional<Usuario> getUsuarioByUsername (String username) {
        return UsuarioRepository.findById(username);
    }

    public List<Usuario> getUsuariosByPapel (String papel) {
        return UsuarioRepository.getUsusariosByPapel(papel);
    }

    public void deleteAll() {
        UsuarioRepository.deleteAll();
    }

    public List<Usuario> findAll() {
        return UsuarioRepository.findAll();
    }

    public boolean validate(String username, String password) {
        Optional<Usuario> user = getUsuarioByUsername(username);
        if (user.isPresent() && user.get().getAtiva())
            return user.isPresent() && user.get().getPassword().equals(password);
        return false;
    }

    public void change(String username) {
        Optional<Usuario> user = getUsuarioByUsername(username);
        System.out.println(!user.get().getAtiva());
        if (user.isPresent())
            user.get().setAtiva((!user.get().getAtiva()));
        UsuarioRepository.save(user.get());
        
    }
}