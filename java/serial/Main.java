package serial;

import serial.utils.FitnessEvaluator;
import serial.utils.Biomarcador;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String caminhoArquivo = "data/biomarcadores_1gb.txt";
        int tamanhoPopulacao = 100;

        List<Biomarcador> dados = FitnessEvaluator.carregar(caminhoArquivo);
        GeneticAlgorithm ga = new GeneticAlgorithm(dados, tamanhoPopulacao);
        ga.executar();
    }
}
