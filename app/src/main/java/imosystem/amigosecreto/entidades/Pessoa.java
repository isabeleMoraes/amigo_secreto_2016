package imosystem.amigosecreto.entidades;

public class Pessoa {
    private String nome;
    private String celular;
    private Pessoa Amigo;

    public Pessoa(String nome, String celular) {
        this.nome = nome;
        setCelular(celular);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {

        //this.celular = Integer.parseInt(celular.replaceAll("[^0-9]",""));
        this.celular = celular.replaceAll("([^0-9])","");
        //this.celular = celular;
    }

    public Pessoa getAmigo() {
        return Amigo;
    }

    public void setAmigo(Pessoa amigo) {
        Amigo = amigo;
    }
}
