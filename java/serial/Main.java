package serial;

import serial.utils.FitnessEvaluator;
import serial.utils.Biomarcador;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String caminhoArquivo = "data/biomarcadores_1gb.txt";
        int tamanhoPopulacao = 100;
        String logPath = "serial_execution_log2.txt";

        try (PrintWriter log = new PrintWriter(new FileWriter(logPath))) {
            log.println("==== EXECUÇÃO SERIAL DO ALGORITMO GENÉTICO ====\n");

            log.println("Iniciando carregamento do dataset...");
            long inicioCarga = System.nanoTime();
            List<Biomarcador> dados = FitnessEvaluator.carregar(caminhoArquivo);
            long fimCarga = System.nanoTime();
            log.printf("Total de biomarcadores: %d\n", dados.size());
            log.printf("Tempo de carregamento: %.3f segundos\n\n", (fimCarga - inicioCarga) / 1e9);

            log.println("Iniciando execução do algoritmo genético...\n");

            // Captura de uso de memória antes da execução
            Runtime runtime = Runtime.getRuntime();
            runtime.gc(); // força coleta de lixo
            long memoriaAntes = runtime.totalMemory() - runtime.freeMemory();

            long inicio = System.nanoTime();
            GeneticAlgorithm ga = new GeneticAlgorithm(dados, tamanhoPopulacao);
            ga.executar();
            long fim = System.nanoTime();

            long memoriaDepois = runtime.totalMemory() - runtime.freeMemory();
            long memoriaMax = runtime.maxMemory();

            double tempoTotal = (fim - inicio) / 1e9;

            log.printf("Tempo de execução do algoritmo: %.3f segundos\n", tempoTotal);
            log.printf("Memória antes: %.2f MB\n", memoriaAntes / (1024.0 * 1024));
            log.printf("Memória depois: %.2f MB\n", memoriaDepois / (1024.0 * 1024));
            log.printf("Memória máxima disponível: %.2f MB\n", memoriaMax / (1024.0 * 1024));

            log.println("\n==== FIM DA EXECUÇÃO ====");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
