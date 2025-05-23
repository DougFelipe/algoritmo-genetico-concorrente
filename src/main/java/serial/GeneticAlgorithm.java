package serial;

import serial.utils.Biomarcador;
import serial.utils.FitnessEvaluator;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
    private final List<Biomarcador> dados;
    private final int tamanhoPopulacao;
    private final List<BitSet> populacao;

    public GeneticAlgorithm(List<Biomarcador> dados, int tamanhoPopulacao) {
        this.dados = dados;
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.populacao = gerarPopulacao();
    }

    private List<BitSet> gerarPopulacao() {
        List<BitSet> lista = new ArrayList<>();
        Random rand = new Random();
        int tamanhoCromossomo = dados.size();

        for (int i = 0; i < tamanhoPopulacao; i++) {
            BitSet cromossomo = new BitSet(tamanhoCromossomo);
            for (int j = 0; j < tamanhoCromossomo; j++) {
                if (rand.nextBoolean()) {
                    cromossomo.set(j);
                }
            }
            lista.add(cromossomo);
        }

        return lista;
    }

    public void executar() {
        double melhorFitness = Double.NEGATIVE_INFINITY;
        BitSet melhorIndividuo = null;

        for (BitSet individuo : populacao) {
            double fit = FitnessEvaluator.avaliar(individuo, dados);
            if (fit > melhorFitness) {
                melhorFitness = fit;
                melhorIndividuo = (BitSet) individuo.clone(); // evitar referência compartilhada
            }
        }

        System.out.printf("Melhor fitness encontrado: %.2f\n", melhorFitness);
    }
}
