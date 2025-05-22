package serial.utils;

public class Biomarcador {
    public String id;
    public double expressao;
    public int conservacao;
    public int similaridadeHumana;
    public Localizacao localizacao;
    
    public Biomarcador(String id, double expressao, int conservacao, int similaridadeHumana, Localizacao localizacao) {
        this.id = id;
        this.expressao = expressao;
        this.conservacao = conservacao;
        this.similaridadeHumana = similaridadeHumana;
        this.localizacao = localizacao;
    }
}
