package serial.utils;

public class Biomarcador {
    public String id;
    public double expressao;
    public int conservacao;
    public int similaridadeHumana;
    public String localizacao;

    public Biomarcador(String id, double expressao, int conservacao, int similaridadeHumana, String localizacao) {
        this.id = id;
        this.expressao = expressao;
        this.conservacao = conservacao;
        this.similaridadeHumana = similaridadeHumana;
        this.localizacao = localizacao;
    }
}
