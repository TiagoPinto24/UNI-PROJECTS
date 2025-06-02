package TW.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TW.Enteties.Usuario;
import TW.Enteties.Viagem;
import TW.repositories.ViagemRepository;

@Service
public class ViagemService {
    @Autowired
    private ViagemRepository ViagemRepository;

    
    public Viagem addViagem(Viagem viagem) {
        return ViagemRepository.save(viagem);
    }
    
    public Boolean deletebyID(int id, String username) {
        if (ViagemRepository.findById(id).get().getIdCondutor().equals(username)){
            ViagemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void deleteAll() {
        ViagemRepository.deleteAll();
    }

    public List<Viagem> getAll() {
        return ViagemRepository.findAll();        
    }


    public Optional<Viagem> getViagembyID(int id) {
        return ViagemRepository.findById(id);
    }
   
    public void joinViagem(int id, Usuario usuario) {
        Viagem viagem = ViagemRepository.findById(id).get();
        viagem.addPassageiro(usuario);
        ViagemRepository.save(viagem);
    }

    public void leaveViagem(int id, Usuario usuario) {
        Viagem viagem = ViagemRepository.findById(id).get();
        viagem.removePassageiro(usuario);
        ViagemRepository.save(viagem);
    }
}
