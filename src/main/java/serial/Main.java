package serial;

import serial.utils.FitnessEvaluator;
import serial.utils.Biomarcador;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso: java -cp out serial.Main <caminho_dataset> <tamanho_populacao>");
            System.exit(1);
        }

        String caminhoArquivo = args[0];
        int tamanhoPopulacao = Integer.parseInt(args[1]);

        List<Biomarcador> dados = FitnessEvaluator.carregar(caminhoArquivo);

        long inicio = System.nanoTime();
        GeneticAlgorithm ga = new GeneticAlgorithm(dados, tamanhoPopulacao);
        ga.executar();
        long fim = System.nanoTime();

        double tempoTotal = (fim - inicio) / 1e9;
        System.out.printf("Tempo total de execução: %.3f segundos\n", tempoTotal);
    }
}
