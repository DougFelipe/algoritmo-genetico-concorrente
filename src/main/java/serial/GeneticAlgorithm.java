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
    private final int numGenerations;
    private final double taxaCrossover;
    private final double taxaMutacao;

    private final Random rand = new Random();

    public GeneticAlgorithm(List<Biomarcador> dados, int tamanhoPopulacao) {
        this(dados, tamanhoPopulacao, 50, 0.8, 0.01); // valores padrão
    }

    public GeneticAlgorithm(List<Biomarcador> dados, int tamanhoPopulacao, int numGenerations, double taxaCrossover, double taxaMutacao) {
        this.dados = dados;
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.numGenerations = numGenerations;
        this.taxaCrossover = taxaCrossover;
        this.taxaMutacao = taxaMutacao;
    }

    public void executar() {
        List<BitSet> populacao = gerarPopulacaoExterno();
        BitSet melhorIndividuo = null;
        double melhorFitness = Double.NEGATIVE_INFINITY;

        for (int gen = 0; gen < numGenerations; gen++) {
            List<BitSet> novaPopulacao = new ArrayList<>();

            while (novaPopulacao.size() < tamanhoPopulacao) {
                BitSet pai1 = selecaoTorneioExterno(populacao);
                BitSet pai2 = selecaoTorneioExterno(populacao);

                BitSet[] filhos;
                if (rand.nextDouble() < taxaCrossover) {
                    filhos = crossover1PontoExterno(pai1, pai2);
                } else {
                    filhos = new BitSet[]{(BitSet) pai1.clone(), (BitSet) pai2.clone()};
                }

                for (BitSet filho : filhos) {
                    mutacaoExterno(filho);
                    novaPopulacao.add(filho);

                    double fit = FitnessEvaluator.avaliar(filho, dados);
                    if (fit > melhorFitness) {
                        melhorFitness = fit;
                        melhorIndividuo = (BitSet) filho.clone();
                    }

                    if (novaPopulacao.size() == tamanhoPopulacao) break;
                }
            }

            populacao = novaPopulacao;
        }

        System.out.printf("Melhor fitness encontrado: %.2f\n", melhorFitness);
    }

    // === Métodos expostos para benchmarking ===

    public List<BitSet> gerarPopulacaoExterno() {
        return gerarPopulacao();
    }

    public BitSet selecaoTorneioExterno(List<BitSet> populacao) {
        return selecaoTorneio(populacao);
    }

    public BitSet[] crossover1PontoExterno(BitSet p1, BitSet p2) {
        return crossover1Ponto(p1, p2);
    }

    public void mutacaoExterno(BitSet cromossomo) {
        mutacao(cromossomo);
    }

    // === Métodos internos ===

    private List<BitSet> gerarPopulacao() {
        List<BitSet> lista = new ArrayList<>();
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

    private BitSet selecaoTorneio(List<BitSet> populacao) {
        BitSet a = populacao.get(rand.nextInt(populacao.size()));
        BitSet b = populacao.get(rand.nextInt(populacao.size()));
        return FitnessEvaluator.avaliar(a, dados) > FitnessEvaluator.avaliar(b, dados) ? a : b;
    }

    private BitSet[] crossover1Ponto(BitSet p1, BitSet p2) {
        int len = dados.size();
        int ponto = rand.nextInt(len);
        BitSet f1 = new BitSet(len);
        BitSet f2 = new BitSet(len);

        for (int i = 0; i < len; i++) {
            if (i < ponto) {
                if (p1.get(i)) f1.set(i);
                if (p2.get(i)) f2.set(i);
            } else {
                if (p2.get(i)) f1.set(i);
                if (p1.get(i)) f2.set(i);
            }
        }

        return new BitSet[]{f1, f2};
    }

    private void mutacao(BitSet cromossomo) {
        for (int i = 0; i < dados.size(); i++) {
            if (rand.nextDouble() < taxaMutacao) {
                cromossomo.flip(i);
            }
        }
    }
}
