package serial;

import serial.utils.Biomarcador;
import serial.utils.FitnessEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
    private final List<Biomarcador> dados;
    private final int tamanhoPopulacao;
    private final List<boolean[]> populacao;

    public GeneticAlgorithm(List<Biomarcador> dados, int tamanhoPopulacao) {
        this.dados = dados;
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.populacao = gerarPopulacao();
    }

    private List<boolean[]> gerarPopulacao() {
        List<boolean[]> lista = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < tamanhoPopulacao; i++) {
            boolean[] cromossomo = new boolean[dados.size()];
            for (int j = 0; j < cromossomo.length; j++) {
                cromossomo[j] = rand.nextBoolean();
            }
            lista.add(cromossomo);
        }
        return lista;
    }

    public void executar() {
        double melhorFitness = Double.NEGATIVE_INFINITY;
        boolean[] melhorIndividuo = null;

        for (boolean[] individuo : populacao) {
            double fit = FitnessEvaluator.avaliar(individuo, dados);
            if (fit > melhorFitness) {
                melhorFitness = fit;
                melhorIndividuo = individuo;
            }
        }

        System.out.printf("Melhor fitness encontrado: %.2f\n", melhorFitness);
    }
}
