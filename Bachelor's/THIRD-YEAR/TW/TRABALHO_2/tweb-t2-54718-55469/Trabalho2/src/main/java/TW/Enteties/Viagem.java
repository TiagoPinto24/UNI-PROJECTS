package TW.Enteties;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "viagem")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Viagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_condutor")
    private String idCondutor;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_viagem",
        joinColumns = @JoinColumn(name = "viagem_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_username")
    )
    @JsonManagedReference
    private List<Usuario> passageiros = new ArrayList<>();    

    private String origem;
    private String destino;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date data;

    public Viagem(String condutor, String origem, String destino, Date data) {
        this.idCondutor = condutor;
        this.origem = origem;
        this.destino = destino;
        this.data = data;
    }

    public Viagem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdCondutor() {
        return idCondutor;
    }

    public void setIdCondutor(String idCondutor) {
        this.idCondutor = idCondutor;
    }

    public List<Usuario> getPassageiros() {
        return passageiros;
    }

    public void setPassageiros(List<Usuario> passageiros) {
        this.passageiros = passageiros;
    }

    public void addPassageiro(Usuario passageiro) {
        this.passageiros.add(passageiro);
        passageiro.getViagens().add(this);
    }

    public void removePassageiro(Usuario passageiro) {
        this.passageiros.remove(passageiro);
        passageiro.getViagens().remove(this);
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}