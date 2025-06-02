package TW.Enteties;

import java.sql.Date;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_passageiro")
    private String passageiro;

    private String origem;
    private String destino;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date data;

    public Pedido(int ID_condutor, String passageiro, ArrayList<Integer> passageiros, String origem, String destino, Date data) {
        super();
        this.passageiro = passageiro;
        this.origem = origem;
        this.destino = destino;
        this.data = data;
    }

    public Pedido() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(String passageiro) {
        this.passageiro = passageiro;
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

    public Date getDate() {
        return data;
    }

    public void setDate(Date data) {
        this.data = data;
    }
}
